package com.iam.oneom.pages.main.episodes;

import android.content.Context;
import android.support.annotation.NonNull;

import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.network.Web;
import com.iam.oneom.core.rx.RxBus;
import com.iam.oneom.core.rx.UpdateFinishedEvent;
import com.iam.oneom.core.update.Updater;
import com.iam.oneom.core.util.Time;
import com.iam.oneom.pages.mpd.PagingDataSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.RealmQuery;
import io.realm.Sort;

/**
 * Created by iam on 08.04.17.
 */

class EpisodesDataSource implements PagingDataSource<Episode> {

    OnLoadListener<Episode> l;

    public EpisodesDataSource(OnLoadListener<Episode> l) {
        this.l = l;
        RxBus.INSTANCE.register(UpdateFinishedEvent.class,
                updateFinishedEvent -> {
                    if (l != null) {
                        l.onLoad(new ArrayList<>());
                    }
                    if (updateFinishedEvent.getThrowable() != null && l != null) {
                        l.onError(updateFinishedEvent.getThrowable());
                    }
                });
    }

    @Override
    public void getPrevious() {
        Number updatedAt = query().min("updatedAt");
        long end = updatedAt.longValue();
        if (end <= 0) {
            return;
        }

        long start = end - Time.MONTH;
        if (start <= 0) {
            return;
        }

        Web.instance.getLastEpisodesByDate(Time.toString(start), Time.toString(end))
                .onErrorReturn(throwable -> {
                    if (l != null) {
                        l.onError(throwable);
                    }
                    return null;
                })
                .subscribe(
                        epsDateResponse -> {

                            if (epsDateResponse == null) {
                                return;
                            }

                            DbHelper.insertAll(epsDateResponse.getEps());
                            if (l != null) {
                                l.onLoad(epsDateResponse.getEps());
                            }
                        }
                );
    }

    @Override
    public void getModels() {
        List<Episode> episodes = query()
                .findAllSorted("airdate", Sort.DESCENDING);
        if (l != null) {
            l.onLoad(episodes);
        }
    }

    @NonNull
    private RealmQuery<Episode> query() {
        return DbHelper.where(Episode.class)
                .lessThan("airdate", new Date(new Date().getTime() + Time.YEAR))
                .greaterThan("airdate", new Date(new Date().getTime() - 2 * Time.MONTH))
                ;
    }

    @Override
    public void refresh(Context context) {
        Updater.update(context);
    }

}

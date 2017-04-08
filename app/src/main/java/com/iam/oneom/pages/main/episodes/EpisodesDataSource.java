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
import rx.Subscription;

/**
 * Created by iam on 08.04.17.
 */

class EpisodesDataSource implements PagingDataSource<Episode> {

    private OnLoadListener<Episode> l;
    private Subscription subscription;

    public EpisodesDataSource(OnLoadListener<Episode> l) {
        this.l = l;
        subscription = RxBus.INSTANCE.register(UpdateFinishedEvent.class,
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
        Number updatedAt = query().min("airdate");
        long end = updatedAt.longValue();
        if (end <= 0) {
            return;
        }

        long start = end - 2 * Time.MONTH;
        if (start <= 0) {
            start = 0;
        }

        List<Episode> episodes = query()
                .lessThan("airdate", end)
                .greaterThan("airdate", start)
                .findAll();

        if (episodes != null && episodes.size() != 0) {
            if (l != null) {
                l.onLoad(episodes);
            }
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
                .lessThan("updatedAt", new Date(new Date().getTime() + Time.YEAR).getTime())
                .greaterThan("updatedAt", new Date(new Date().getTime() - 2 * Time.MONTH).getTime())
                .findAll();
        if (l != null) {
            l.onLoad(episodes);
        }
    }

    @Override
    public void unsubscribe() {
        subscription.unsubscribe();
    }

    @NonNull
    private RealmQuery<Episode> query() {
        return DbHelper.where(Episode.class);
    }

    @Override
    public void refresh(Context context) {
        Updater.update(context);
    }

}

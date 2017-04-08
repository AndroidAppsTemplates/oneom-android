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
    private long earlierUpdatedAt;

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
        long end = earlierUpdatedAt;
        if (end <= 0) {
            return;
        }

        long start = end - 2 * Time.MONTH;
        if (start <= 0) {
            start = 0;
        }

        RealmQuery<Episode> episodeRealmQuery = query()
                .lessThan("airdate", end)
                .greaterThan("airdate", start);

        List<Episode> episodes = episodeRealmQuery.findAll();

        if (episodes != null && episodes.size() != 0) {
            if (l != null) {
                l.onLoad(episodes);
                earlierUpdatedAt = episodeRealmQuery.min("airdate").longValue();
            }
            return;
        }

        getFromWeb(end, start);
    }

    private void getFromWeb(long end, long start) {
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

                            if (l != null) {
                                l.onLoad(epsDateResponse.getEps());
                            }
                            for (Episode episode : epsDateResponse.getEps()) {
                                if (episode.getUpdatedAt() < earlierUpdatedAt) {
                                    earlierUpdatedAt = episode.getUpdatedAt();
                                    episode.setIsSheldule(true);
                                }
                            }
                            DbHelper.insertAll(epsDateResponse.getEps());

                        }
                );
    }

    @Override
    public void getModels() {
        RealmQuery<Episode> episodeRealmQuery = query()
                .lessThan("airdate", new Date(new Date().getTime() + Time.YEAR).getTime())
                .greaterThan("airdate", new Date(new Date().getTime() - 2 * Time.MONTH).getTime());

        List<Episode> episodes = episodeRealmQuery.findAll();
        if (l != null) {
            l.onLoad(episodes);
            earlierUpdatedAt = episodeRealmQuery.min("airdate").longValue();
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

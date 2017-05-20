package com.iam.oneom.pages.main.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;
import android.widget.Toast;

import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.DbUtil;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.network.Web;
import com.iam.oneom.core.util.Decorator;
import com.iam.oneom.core.util.RxUtils;
import com.iam.oneom.pages.main.SerialPageActivity;
import com.iam.oneom.pages.main.episode.EpisodeSearchActivity;

import rx.Subscription;

/**
 * Created by iam on 20.05.17.
 */

public class EpisodeViewModel {

    private Subscription subscription;

    public ObservableField<Episode> episode = new ObservableField<>();
    public String posterUrl;
    public ObservableBoolean loadingEpisode = new ObservableBoolean();

    public EpisodeViewModel(Episode episode) {
        this.episode.set(episode);
        posterUrl = DbUtil.posterUrl(episode, Decorator.MAX);
    }


    public void onResume() {
        loadingEpisode.set(true);
        subscription = Web.instance.getEpisode(episode.get().getId())
                .subscribe(
                        epResponse -> {
                            loadingEpisode.set(false);
                            episode.set(epResponse.getEpisode());
                            DbHelper.insert(epResponse.getEpisode());
                        }
                );
    }

    public void onDestroy() {
        if (loadingEpisode.get()) {
            loadingEpisode.set(false);
        }
        RxUtils.unsubscribe(subscription);
    }

    public View.OnClickListener onSerialPageClick = v ->
            SerialPageActivity.start(v.getContext(), episode.get().getSerial().getId());

    public View.OnClickListener onRelatedClick = v ->
            Toast.makeText(v.getContext(), "Undefined", Toast.LENGTH_SHORT).show();

    public View.OnClickListener onGoSearchClick = v ->
            EpisodeSearchActivity.start(v.getContext(), episode.get().getId());
}

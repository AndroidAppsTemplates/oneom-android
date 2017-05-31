package com.iam.oneom.binding.episode;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.iam.oneom.BR;
import com.iam.oneom.R;
import com.iam.oneom.binding.OnItemBindClass;
import com.iam.oneom.binding.payload.RelatedHeader;
import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.entities.model.Online;
import com.iam.oneom.core.entities.model.Torrent;
import com.iam.oneom.core.network.Web;
import com.iam.oneom.core.rx.RxUtils;
import com.iam.oneom.pages.main.SerialPageActivity;
import com.iam.oneom.pages.main.episode.EpisodeRelatedItemsActivity;
import com.iam.oneom.pages.main.episode.sources.EpisodeSearchActivity;
import com.iam.oneom.util.Decorator;
import com.iam.oneom.util.Intents;
import com.iam.oneom.util.OneOmUtil;

import me.tatarka.bindingcollectionadapter2.OnItemBind;
import me.tatarka.bindingcollectionadapter2.collections.MergeObservableList;
import rx.Subscription;

/**
 * Created by iam on 20.05.17.
 */

public class EpisodeViewModel {

    private Subscription subscription;

    public ObservableField<Episode> episode = new ObservableField<>();
    public MergeObservableList<Object> relatedItems = new MergeObservableList<>();
    public String posterUrl;
    public ObservableBoolean loadingEpisode = new ObservableBoolean();

    public final OnItemBind<Object> relatedBinding =
            new OnItemBindClass<>()
                    .map(Torrent.class, (itemBinding, position, item)
                            -> itemBinding
                            .set(BR.item, R.layout.torrent_related_item)
                            .bindExtra(BR.vm, this))
                    .map(Online.class, (itemBinding, position, item)
                            -> itemBinding
                            .set(BR.item, R.layout.online_related_item)
                            .bindExtra(BR.vm, this)
                            .bindExtra(BR.isLast, position == episode.get().getOnline().size()))
                    .map(RelatedHeader.class, BR.item, R.layout.header_related_item);

    public EpisodeViewModel(Episode episode) {

        this.episode.set(episode);
        posterUrl = OneOmUtil.posterUrl(this.episode.get(), Decorator.MAX);
        relatedItems
                .insertList(onlinesList())
                .insertList(torrentsList());
    }

    public void onPlayOnlineClick(Context context, Online online) {

        if (online.getVideoUrl() == null || online.getVideoUrl().length() == 0) {
            Toast.makeText(context, R.string.no_direct_link_to_video, Toast.LENGTH_LONG).show();
            return;
        }

        Intents.runOnline(context, online.getVideoUrl());
    }

    public void onOnTorrentDownloadClick(Context context, Torrent torrent) {

        if (torrent.getValue() == null || torrent.getValue().length() == 0) {
            Toast.makeText(context, R.string.no_direct_link_to_torrent, Toast.LENGTH_LONG).show();
            return;
        }

        Uri parse = Uri.parse(torrent.getValue());

        if (parse.getScheme().equals("magnet")) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(parse);
                Intent chooser = Intent.createChooser(intent, context.getString(R.string.select_app));
                context.startActivity(chooser);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Toast
                        .makeText(
                                context,
                                R.string.no_apps,
                                Toast.LENGTH_LONG
                        ).show();
            }
        } else {
            Intents.runWeb(context, torrent.getValue());
        }
    }

    private ObservableList<Object> torrentsList() {
        ObservableList<Object> list = new ObservableArrayList<>();
        if (episode.get().getTorrent() != null && episode.get().getTorrent().size() > 0) {
            list.add(new RelatedHeader("Torrents"));
            list.addAll(episode.get().getTorrent());
        }
        return list;
    }

    private ObservableList<Object> onlinesList() {
        ObservableList<Object> list = new ObservableArrayList<>();
        if (episode.get().getOnline() != null && episode.get().getOnline().size() > 0) {
            list.add(new RelatedHeader("Online"));
            list.addAll(episode.get().getOnline());
        }
        return list;
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

    public View.OnClickListener onRelatedClick = v -> {
        if (OneOmUtil.episodeRelatedCount(episode.get()) <= 0) {
            Toast.makeText(v.getContext(), R.string.no_related_items, Toast.LENGTH_SHORT).show();
        } else {
            EpisodeRelatedItemsActivity.start(v, episode.get().getId());
        }
    };

    public View.OnClickListener onGoSearchClick = v ->
            EpisodeSearchActivity.start(v.getContext(), episode.get().getId());

}

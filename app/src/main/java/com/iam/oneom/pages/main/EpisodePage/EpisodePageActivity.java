package com.iam.oneom.pages.main.EpisodePage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.iam.oneom.R;
import com.iam.oneom.core.entities.Util;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.search.Search;
import com.iam.oneom.core.util.Decorator;
import com.iam.oneom.env.handling.recycler.BindableViewHolder;
import com.iam.oneom.env.handling.recycler.itemdecorations.SpacesBetweenItemsDecoration;
import com.iam.oneom.env.handling.recycler.layoutmanagers.GridLayoutManager;
import com.iam.oneom.env.widget.CircleProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;


public class EpisodePageActivity extends AppCompatActivity {

    Episode episode;
    String searchString;

    private final static String EP_ID_EXTRA = "EP_ID_EXTRA";

    @BindView(R.id.poster)
    ImageView posterImage;
    @BindView(R.id.progress)
    CircleProgressBar cpb;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.bluring_area)
    FrameLayout bluringArea;

    GridLayoutManager layoutManager;
    EpisodePageAdapter episodePageAdapter;

    public static void start(Context context, long id) {
        Intent intent = new Intent(context, EpisodePageActivity.class);
        intent.putExtra(EP_ID_EXTRA, id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_page_activity);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        long id = intent.getExtras().getLong(EP_ID_EXTRA, 0);
        episode = Realm.getDefaultInstance().where(Episode.class).equalTo("id", id).findFirst();
        loadBackground(Util.posterUrl(episode, Decorator.MAX));
        searchString = episode.getSerial().getTitle();
        configureRecycler();
    }

    class EpisodePageAdapter extends RecyclerView.Adapter<BindableViewHolder> {

        private static final int HEADER = 0;
        private static final int ONLINE = 1;
        private static final int TORRENT = 2;
        private static final int SUBTITLES = 3;

        LayoutInflater inflater;

        EpisodePageAdapter(Context context, Episode episode) {
            inflater = ((Activity)context).getLayoutInflater();
        }

        @Override
        public BindableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case HEADER:
                    return new EpisodesHeaderVH(inflater.inflate(R.layout.media_page_header_ep, parent, false), episode);
                case ONLINE:
                    return new OnlineSearchVH(inflater.inflate(R.layout.media_page_episode_search_online, parent, false), cpb, episode);
                case TORRENT:
                    return new TorrentSearchVH(inflater.inflate(R.layout.media_page_episode_search_torrent, parent, false), cpb, episode);
                case SUBTITLES:
                    return new SubtitlesSearchVH(inflater.inflate(R.layout.media_page_episode_search_subtitle, parent, false), cpb, episode);
            }
            throw new RuntimeException("EpisodePageActivity.EpisodePageActivityAdapter has not view type with tag" + viewType);
        }

        @Override
        public int getItemViewType(int position) {
            if (isHeader(position)) {
                return HEADER;
            }  else if (isTorrentSearch(position)) {
                return TORRENT;
            } else if (isSubtitlesSearch(position)){
                return SUBTITLES;
            } else {
                return ONLINE;
            }
        }

        @Override
        public void onBindViewHolder(BindableViewHolder holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return 4;
        }
        public boolean isHeader(int position) {
            return position == 0;
        }
        public boolean isTorrentSearch(int position) {
            return position == 1;
        }
        public boolean isSubtitlesSearch(int position) {
            return position == 2;
        }
        public boolean isOnlineSearch(int position) {
            return position == 3;
        }
    }

    private void configureRecycler() {
        episodePageAdapter = new EpisodePageAdapter(this, episode);
        recycler.setAdapter(episodePageAdapter);
        layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return episodePageAdapter.isHeader(position) ||
                        episodePageAdapter.isOnlineSearch(position) ||
                        episodePageAdapter.isTorrentSearch(position) ||
                        episodePageAdapter.isSubtitlesSearch(position)
                                                    ?
                                        layoutManager.getSpanCount() : 1;
            }
        });
        recycler.addItemDecoration(new SpacesBetweenItemsDecoration((int) Decorator.dipToPixels(this, 8)));
        recycler.setLayoutManager(layoutManager);
    }

    @Override
    public void onBackPressed() {
        Search.instance().reset();
        super.onBackPressed();
    }

    private void loadBackground(String url) {
        Glide
                .with(this)
                .load(url)
                .asBitmap()
                .centerCrop()
                .into(new BitmapImageViewTarget(posterImage) {
                    @Override
                    protected void setResource(Bitmap resource) {

                        long averageColorInt = Decorator.getAverageColorInt(resource);

                        Decorator.setStatusBarColor(EpisodePageActivity.this, (int) averageColorInt);
                        bluringArea.setBackgroundColor((int) (0xE0000000 + averageColorInt));

                        Bitmap blurBitmap = Decorator.fastblur(resource, 1, 25);

                        posterImage.setImageBitmap(blurBitmap);

                    }
                });
    }
    private void showProgressBar() {
        cpb.setVisibility(View.VISIBLE);
    }
    private void hideProgressBar() {
        cpb.setVisibility(View.INVISIBLE);
    }
}
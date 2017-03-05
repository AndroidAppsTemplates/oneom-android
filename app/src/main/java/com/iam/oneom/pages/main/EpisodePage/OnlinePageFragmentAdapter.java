package com.iam.oneom.pages.main.EpisodePage;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.iam.oneom.R;
import com.iam.oneom.core.entities.Util;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.entities.model.Online;
import com.iam.oneom.core.entities.model.Source;
import com.iam.oneom.core.rx.EpisodeDataReceivedEvent;
import com.iam.oneom.core.rx.RxBus;
import com.iam.oneom.core.util.Decorator;
import com.iam.oneom.env.handling.recycler.BindableViewHolder;
import com.iam.oneom.env.handling.recycler.itemdecorations.SpacesBetweenItemsDecoration;
import com.iam.oneom.env.handling.recycler.layoutmanagers.LinearLayoutManager;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;


public class OnlinePageFragmentAdapter extends EpisodePageNestedChildAdapter {

    private Episode episode;
    WeakReference<OnlinePageFragment> fragment;

    @BindDimen(R.dimen.ep_poster_corner_radius)
    int cornerRadius;

    @BindDimen(R.dimen.online_related_spacing)
    int relatedSpacing;


    public OnlinePageFragmentAdapter(OnlinePageFragment fragment, Episode episode) {
        super((AppCompatActivity) fragment.getActivity(), episode);
        this.fragment = new WeakReference<>(fragment);
        this.episode = episode;
        ButterKnife.bind(this, fragment.getActivity());
    }

    @Override
    public void onCreate() {
        RxBus.INSTANCE.register(EpisodeDataReceivedEvent.class, episodeDateReceivedEvent -> {
            episode = episodeDateReceivedEvent.getEpisode();
            fragment.get().view.setAdapter(new Adapter());
            fragment.get().view.setLayoutManager(new LinearLayoutManager(getActivity()));
        });
    }

    public void configureColorSheme() {

    }

    protected class Adapter extends RecyclerView.Adapter<BindableViewHolder> {

        private static final int HEADER = 0;
        private static final int ITEM = 1;

        List<Source> sources;

        private int offset;

        protected Adapter() {
            sources = Realm.getDefaultInstance().where(Source.class).equalTo("typeId", Source.ONLINE).findAll();
            offset = isRelatedExists() ? 0 : 1;
        }

        @Override
        public BindableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == HEADER) {
                return new HeaderVH(LayoutInflater.from(getActivity()).inflate(R.layout.episode_page_online_header_item, parent, false));
            }

            return null;
        }

        @Override
        public void onBindViewHolder(BindableViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return offset;
        }

        private boolean isRelatedExists() {
            return episode.getOnline() == null || episode.getOnline().size() == 0;
        }

        @Override
        public int getItemViewType(int position) {
            return isRelatedExists() && position == 0 ? HEADER : ITEM;
        }

        protected class HeaderVH extends BindableViewHolder {

            @BindView(R.id.recycler)
            RecyclerView recyclerView;

            public HeaderVH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), android.support.v7.widget.LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(new HeaderAdapter(episode.getOnline()));
                recyclerView.addItemDecoration(new SpacesBetweenItemsDecoration(relatedSpacing));
            }

            @Override
            public void onBind(int position) {

            }
        }
    }

    protected class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.OnlineVH> {

        private List<Online> onlines;

        protected HeaderAdapter(List<Online> onlines) {
            this.onlines = onlines;
        }

        @Override
        public OnlineVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new OnlineVH(LayoutInflater.from(getActivity()).inflate(R.layout.episode_page_online_item, parent, false));
        }

        @Override
        public void onBindViewHolder(OnlineVH holder, int position) {
            Glide.with(getActivity())
                    .load(Util.posterUrl(getEpisode(), Decorator.W480))
                    .asBitmap()
                    .into(new BitmapImageViewTarget(holder.poster) {

                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(fragment.get().getResources(), resource);
                            circularBitmapDrawable.setCornerRadius(cornerRadius);

                            holder.poster.setImageDrawable(circularBitmapDrawable);
                        }
                    });
            holder.quality.setText(Util.qualityTag(onlines.get(position)));
        }

        @Override
        public int getItemCount() {
            return onlines.size();
        }

        protected class OnlineVH extends BindableViewHolder {

            @BindView(R.id.poster)
            protected ImageView poster;
            @BindView(R.id.quality)
            protected TextView quality;

            public OnlineVH(View itemView) {
                super(itemView);
            }

            @Override
            public void onBind(int position) {

            }
        }
    }
}
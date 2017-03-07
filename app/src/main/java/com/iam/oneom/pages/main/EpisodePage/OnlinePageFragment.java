package com.iam.oneom.pages.main.EpisodePage;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.iam.oneom.R;
import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.Util;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.entities.model.Online;
import com.iam.oneom.core.entities.model.Source;
import com.iam.oneom.core.network.Web;
import com.iam.oneom.core.network.response.EpResponse;
import com.iam.oneom.core.rx.EpisodeImageTintDefinedEvent;
import com.iam.oneom.core.rx.RxBus;
import com.iam.oneom.core.util.Decorator;
import com.iam.oneom.env.handling.recycler.BindableViewHolder;
import com.iam.oneom.env.handling.recycler.itemdecorations.SpacesBetweenItemsDecoration;
import com.iam.oneom.env.handling.recycler.layoutmanagers.LinearLayoutManager;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnlinePageFragment extends Fragment implements Callback<EpResponse> {

    private static final String ID_EXTRA = "ID_EXTRA";

    private Episode episode;

    private RecyclerView recyclerView;

    @BindDimen(R.dimen.ep_poster_corner_radius)
    int cornerRadius;

    public static OnlinePageFragment getFragment(long id) {
        OnlinePageFragment fragment = new OnlinePageFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ID_EXTRA, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.episode_page_online_page, container, false);
        setData();

        if (episode != null) {
            configureViews();
        }

        RxBus.INSTANCE.register(EpisodeImageTintDefinedEvent.class,
                episodeImageReceivedEvent -> configureColorSheme());

        return recyclerView;
    }

    private void setData() {
        long id = getArguments().getLong(ID_EXTRA);
        episode = DbHelper.where(Episode.class).equalTo("id", id).findFirst();
        Web.instance.getEpisode(id).enqueue(this);
    }

    public void configureViews() {
        recyclerView.setAdapter(new Adapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void configureColorSheme() {

    }

    @Override
    public void onResponse(Call<EpResponse> call, Response<EpResponse> response) {
        this.episode = response.body().getEpisode();
        configureViews();
    }

    @Override
    public void onFailure(Call<EpResponse> call, Throwable t) {

    }


    protected class Adapter extends RecyclerView.Adapter<BindableViewHolder> {

        private static final int HEADER = 0;
        private static final int ITEM = 1;

        List<Source> sources;

        private int offset;

        protected Adapter() {
            sources = DbHelper.where(Source.class).equalTo("typeId", Source.ONLINE).findAll();
            offset = isRelatedExists() ? 1 : 0;
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
            return episode.getOnline() != null && episode.getOnline().size() != 0;
        }

        @Override
        public int getItemViewType(int position) {
            return isRelatedExists() && position == 0 ? HEADER : ITEM;
        }

        protected class HeaderVH extends BindableViewHolder {

            @BindView(R.id.recycler)
            RecyclerView recyclerView;

            @BindDimen(R.dimen.online_related_spacing)
            int relatedSpacing;

            public HeaderVH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                recyclerView.addItemDecoration(new SpacesBetweenItemsDecoration(relatedSpacing));
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), android.support.v7.widget.LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(new HeaderAdapter(episode.getOnline()));
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
                    .load(Util.posterUrl(episode, Decorator.W480))
                    .asBitmap()
                    .error(R.drawable.ic_movie_black_48dp)
                    .centerCrop()
                    .into(new BitmapImageViewTarget(holder.poster) {

                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
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
                ButterKnife.bind(this, itemView);
            }

            @Override
            public void onBind(int position) {

            }
        }
    }
}

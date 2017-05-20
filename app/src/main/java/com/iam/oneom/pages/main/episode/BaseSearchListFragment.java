package com.iam.oneom.pages.main.episode;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.iam.oneom.R;
import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.DbUtil;
import com.iam.oneom.core.entities.HasUrl;
import com.iam.oneom.core.entities.Tagged;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.entities.model.Source;
import com.iam.oneom.core.util.Decorator;
import com.iam.oneom.env.handling.recycler.BindableViewHolder;
import com.iam.oneom.env.handling.recycler.itemdecorations.SpacesBetweenItemsDecoration;
import com.iam.oneom.env.handling.recycler.layoutmanagers.LinearLayoutManager;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseSearchListFragment extends Fragment {

    private static final String ID_EXTRA = "ID_EXTRA";

    private Episode episode;

    private RecyclerView recyclerView;
    private Adapter adapter;

    @BindDimen(R.dimen.ep_poster_corner_radius)
    protected int cornerRadius;

    @BindColor(R.color.light)
    protected int lightColor;
    @BindColor(R.color.dark)
    protected int darkColor;
    @BindColor(R.color.middle)
    protected int middleColor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.episode_page_online_page, container, false);
        ButterKnife.bind(this, recyclerView);

        setData();

        if (episode != null) {
            configureViews();
        }

        return recyclerView;
    }

    private void setData() {
        long id = getArguments().getLong(ID_EXTRA);
        episode = DbHelper.where(Episode.class).equalTo("id", id).findFirst();
    }

    public void configureViews() {
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    protected Episode getEpisode() {
        return episode;
    }

    protected class Adapter extends RecyclerView.Adapter<BindableViewHolder> {

        private static final int HEADER = 0;
        private static final int ITEM = 1;

        private int tintColor = 0;

        List<Source> sources;

        private int offset;

        protected Adapter() {
            sources = getSources();
            offset = isRelatedExists() ? 1 : 0;
        }

        @Override
        public BindableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == HEADER) {
                return new HeaderVH(LayoutInflater.from(getActivity()).inflate(R.layout.episode_page_online_header_item, parent, false));
            }

            return new ItemVH(LayoutInflater.from(getActivity()).inflate(R.layout.episode_page_online_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(BindableViewHolder holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return offset + sources.size();
        }

        private boolean isRelatedExists() {
            return getRelatedItems() != null && getRelatedItems().size() != 0;
        }

        @Override
        public int getItemViewType(int position) {
            return isRelatedExists() && position == 0 ? HEADER : ITEM;
        }

        protected class HeaderVH extends BindableViewHolder {

            @BindView(R.id.recycler)
            RecyclerView recyclerView;
            @BindView(R.id.related)
            TextView related;

            @BindDimen(R.dimen.online_related_spacing)
            int relatedSpacing;

            HeaderAdapter adapter;

            public HeaderVH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                adapter = new HeaderAdapter(getRelatedItems());
                recyclerView.addItemDecoration(new SpacesBetweenItemsDecoration(relatedSpacing));
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), android.support.v7.widget.LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onBind(int position) {

            }
        }

        protected class ItemVH extends BindableViewHolder {

            @BindView(R.id.divider)
            View divider;
            @BindView(R.id.source)
            TextView textView;
            @BindView(R.id.icon_next)
            ImageView iconNext;
            @BindColor(R.color.white)
            int active;
            @BindColor(R.color.half_gray)
            int not_active;

            private View view;

            public ItemVH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                this.view = itemView;
            }

            @Override
            public void onBind(int position) {
                Source source = sources.get(isRelatedExists() ? position - 1 : position);
                textView.setText(source.getName());
                textView.setTextColor(DbUtil.isEmptySource(source) ? not_active : active);
                divider.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
                view.setOnClickListener(v -> {
                    if (DbUtil.isEmptySource(source)) {
                        Toast.makeText(getActivity(), getString(R.string.no_searh_data, source.getName()), Toast.LENGTH_LONG).show();
                    } else {
                        startNextActivity(source);
                    }
                });
            }
        }
    }

    protected abstract void startNextActivity(Source source);

    protected abstract List<? extends Tagged> getRelatedItems();

    protected abstract List<Source> getSources();

    protected class HeaderAdapter<T extends Tagged & HasUrl> extends RecyclerView.Adapter<HeaderAdapter.ItemVH> {

        private List<T> tagged;

        protected HeaderAdapter(List<T> tagged) {
            this.tagged = tagged;
        }

        @Override
        public HeaderAdapter.ItemVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemVH(LayoutInflater.from(getActivity()).inflate(R.layout.episode_page_online_item, parent, false));
        }

        @Override
        public void onBindViewHolder(HeaderAdapter.ItemVH holder, int position) {
            Glide.with(getActivity())
                    .load(DbUtil.posterUrl(episode, Decorator.W480))
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

            holder.quality.setText(getRelatedText(tagged.get(position)));
        }

        @Override
        public int getItemCount() {
            return tagged.size();
        }

        protected class ItemVH extends BindableViewHolder {

            @BindView(R.id.poster)
            protected ImageView poster;
            @BindView(R.id.quality)
            protected TextView quality;

            public ItemVH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            @Override
            public void onBind(int position) {

            }
        }
    }

    protected abstract <T extends Tagged> String getRelatedText(T tagged);
}

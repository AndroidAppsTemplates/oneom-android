package com.iam.oneom.pages.main;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iam.oneom.R;
import com.iam.oneom.core.entities.Util;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.entities.model.Torrent;
import com.iam.oneom.pages.main.EpisodePage.EpisodePageActivityNew;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class EpisodeVH extends RecyclerView.ViewHolder {

    protected View view;
    @BindView(R.id.image)
    protected ImageView image;
    @BindView(R.id.title)
    protected TextView title;
    @BindView(R.id.ep)
    protected TextView ep;
    @BindView(R.id.tagBar)
    protected RecyclerView tagbar;

    TagAdapter adapter;

    @BindDimen(R.dimen.episode_item_corner)
    int episode_item_image_corner;

    public EpisodeVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        view = itemView;
    }

    public void onBind(Episode ep) {
        String titleText = ep.getSerial().getTitle();
        title.setText(titleText);
        this.ep.setText(Util.episodeInSeasonString(ep));
        ArrayList<String> tags = new ArrayList<>();
        for (Torrent torrent : ep.getTorrent()) {
            tags.add(Util.qualityTag(torrent));
        }
        boolean isSerialNull = ep.getSerial() == null;
        boolean isPosterNull = ep.getSerial().getPoster() == null;
        Glide
                .with(view.getContext())
                .load(isSerialNull || isPosterNull ? "null" : ep.getSerial().getPoster().getOriginal())
                .error(R.drawable.movie_icon_13)
                .bitmapTransform(new RoundedCornersTransformation(view.getContext(), episode_item_image_corner, 0))
                .into(image);
        view.setOnClickListener(v -> {
            EpisodePageActivityNew.open(v.getContext(), ep.getId());
        });

        tagbar.setAdapter(new TagAdapter(tags));
        tagbar.setLayoutManager(new GridLayoutManager(view.getContext(), 3));

    }

    class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagVH> {

        List<String> strings;

        TagAdapter(List<String> strings) {
            this.strings = strings;
        }

        @Override
        public TagVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TagVH(LayoutInflater.from(view.getContext()).inflate(R.layout.w_tag, parent, false));
        }

        @Override
        public void onBindViewHolder(TagVH holder, int position) {
            holder.onBind(strings.get(position));
        }

        @Override
        public int getItemCount() {
            return strings.size();
        }

        class TagVH extends RecyclerView.ViewHolder {

            TextView tv;

            public TagVH(View itemView) {
                super(itemView);
                tv = (TextView) itemView;
            }

            void onBind(String text) {
                tv.setText(text);
            }
        }
    }
}

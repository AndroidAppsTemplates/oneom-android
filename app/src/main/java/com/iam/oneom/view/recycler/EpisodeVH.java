package com.iam.oneom.view.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iam.oneom.R;
import com.iam.oneom.core.entities.DbUtil;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.util.Decorator;
import com.iam.oneom.pages.main.episode.EpisodePageActivity;

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
    @BindView(R.id.t_appearance_label)
    protected FrameLayout t_appearance_label;
    @BindView(R.id.t_appearance_text)
    protected TextView t_appearance_text;
    @BindView(R.id.o_appearance_label)
    protected FrameLayout o_appearance_label;
    @BindView(R.id.o_appearance_text)
    protected TextView o_appearance_text;
//    @BindView(R.id.tagBar)
//    protected RecyclerView tagbar;

    @BindDimen(R.dimen.episode_item_corner)
    protected int episode_item_image_corner;

    public EpisodeVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        view = itemView;
    }

    public void onBind(Episode ep) {

        this.title.setText(DbUtil.title(ep));
        this.ep.setText(DbUtil.episodeInSeasonString(ep));

        Glide
                .with(view.getContext())
                .load(DbUtil.posterUrl(ep, Decorator.W480))
                .error(R.drawable.ic_movie_dark_24dp)
                .bitmapTransform(new RoundedCornersTransformation(view.getContext(), episode_item_image_corner, 0))
                .into(image);

        view.setOnClickListener(v -> {
            EpisodePageActivity.open(v.getContext(), ep.getId());
        });
        t_appearance_label.setVisibility(DbUtil.hasTorrents(ep) ? View.VISIBLE : View.GONE);
        o_appearance_label.setVisibility(DbUtil.hasOnlines(ep) ? View.VISIBLE : View.GONE);
        t_appearance_text.setText(String.valueOf(DbUtil.torrentsCount(ep)));
        o_appearance_text.setText(String.valueOf(DbUtil.onlinesCount(ep)));
//        tagbar.setAdapter(new TagAdapter(tagbar.getContext(), tags));
//        tagbar.setLayoutManager(new GridLayoutManager(view.getContext(), 3));

    }
}

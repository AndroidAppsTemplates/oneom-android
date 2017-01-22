package com.iam.oneom.pages.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iam.oneom.R;
import com.iam.oneom.core.entities.Util;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.entities.model.Torrent;
import com.iam.oneom.env.widget.TagBar;
import com.iam.oneom.pages.main.EpisodePage.EpisodePageActivity;

import java.util.ArrayList;

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
    protected TagBar tagbar;

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
            EpisodePageActivity.start(v.getContext(), ep.getId());
        });
        tagbar.addTags(tags);
    }
}

package com.iam.oneom.view.recycler;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.iam.oneom.BR;
import com.iam.oneom.R;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.databinding.EpisodesListItemBindBinding;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class BindEpisodeVH extends RecyclerView.ViewHolder {

    private EpisodesListItemBindBinding dataBinding;

    public BindEpisodeVH(View view) {
        super(view);
        this.dataBinding = DataBindingUtil.bind(view);
    }

    public void onBind(Episode ep) {
        dataBinding.setVariable(BR.ep, ep);
    }

    @BindingAdapter({"url", "posterImageCorner"})
    public static void loadImage(ImageView view, String url, float posterImageCorner) {
        Glide
                .with(view.getContext())
                .load(url)
                .error(R.drawable.movie_icon_13)
                .bitmapTransform(new RoundedCornersTransformation(view.getContext(), (int) posterImageCorner, 0))
                .into(view);
    }


}

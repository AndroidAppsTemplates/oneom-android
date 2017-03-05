package com.iam.oneom.pages.main.EpisodePage;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.iam.oneom.R;
import com.iam.oneom.core.entities.Util;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.rx.EpisodeDateReceivedEvent;
import com.iam.oneom.core.rx.RxBus;
import com.iam.oneom.core.util.Decorator;

import java.lang.ref.WeakReference;

import butterknife.BindDimen;
import butterknife.ButterKnife;


public class EpisodePageFragmentAdapter extends EpisodePageNestedChildAdapter {

    private Episode episode;
    WeakReference<EpisodePageFragment> fragment;

    @BindDimen(R.dimen.ep_poster_corner_radius)
    int cornerRadius;


    public EpisodePageFragmentAdapter(EpisodePageFragment fragment, Episode episode) {
        super((AppCompatActivity) fragment.getActivity(), episode);
        this.fragment = new WeakReference<>(fragment);
        this.episode = episode;
        ButterKnife.bind(this, fragment.getActivity());
    }

    @Override
    public void onCreate() {
        RxBus.INSTANCE.register(EpisodeDateReceivedEvent.class, episodeDateReceivedEvent -> {
            episode = episodeDateReceivedEvent.getEpisode();
            fragment.get().description.setText(Util.description(episode, getActivity().getString(R.string.no_description)));

        });

        fragment.get().description.setText(String.format("%s...", fragment.get().getString(R.string.waiting)));

        Glide
                .with(fragment.get())
                .load(Util.posterUrl(episode, Decorator.W480))
                .asBitmap()
                .centerCrop()
                .into(new BitmapImageViewTarget(fragment.get().smallPoster) {
                    @Override
                    protected void setResource(Bitmap resource) {

                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(fragment.get().getResources(), resource);
                        circularBitmapDrawable.setCornerRadius(cornerRadius);

                        fragment.get().smallPoster.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    public void configureColorSheme() {

        int textColor = isLightBackground() ? darkColor : lightColor;

        fragment.get().description.setTextColor(textColor);
        fragment.get().descTitle.setTextColor(textColor);
    }
}
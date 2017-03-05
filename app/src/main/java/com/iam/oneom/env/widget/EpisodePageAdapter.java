package com.iam.oneom.env.widget;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.Html;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.iam.oneom.R;
import com.iam.oneom.core.entities.Util;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.util.Decorator;
import com.iam.oneom.core.util.Time;
import com.iam.oneom.pages.main.EpisodePage.EpisodePageActivityNew;

import java.lang.ref.WeakReference;

import butterknife.BindColor;
import butterknife.ButterKnife;
import io.realm.Realm;

public class EpisodePageAdapter implements RequestListener<String, Bitmap> {

    private WeakReference<EpisodePageActivityNew> activity;
    private boolean lightBackground;
    private int pureColor;
    private int epTint;
    private Episode episode;

    @BindColor(R.color.light)
    int lightColor;
    @BindColor(R.color.dark)
    int darkColor;
    @BindColor(R.color.middle)
    int middleColor;


    public EpisodePageAdapter(EpisodePageActivityNew pageActivityNew) {
        this.activity = new WeakReference<>(pageActivityNew);
        ButterKnife.bind(this, pageActivityNew);
    }

    public void onCreate(Episode episode) {
        this.episode = episode;

        activity.get().description.setText(String.format("%s...", activity.get().getString(R.string.waiting)));

        Glide
                .with(activity.get())
                .load(Util.posterUrl(episode, Decorator.W480))
                .asBitmap()
                .centerCrop()
                .listener(this)
                .into(new BitmapImageViewTarget(activity.get().smallPoster) {
                    @Override
                    protected void setResource(Bitmap resource) {

                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(activity.get().getResources(), resource);
                        circularBitmapDrawable.setCornerRadius(activity.get().cornerRadius);

                        activity.get().smallPoster.setImageDrawable(circularBitmapDrawable);
                    }
                });

        configureColorSheme();

        activity.get().toolbar.setTitle(Util.title(episode));
        activity.get().toolbar.setSubtitle(activity.get().getString(R.string.airdate) + ": " + Time.format(episode.getAirdate(), Time.TimeFormat.TEXT));

        activity.get().blurArea.setBackgroundColor(tint());

        Glide
                .with(activity.get())
                .load(Util.posterUrl(episode, Decorator.MAX))
                .asBitmap()
                .centerCrop()
                .listener(this)
                .into(new BitmapImageViewTarget(activity.get().imagePoster) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        activity.get().imagePoster.setImageBitmap(Decorator.fastblur(resource, 1, 50));
                    }
                });
    }

    public void onEpisodeGetted(Episode episode) {
        if (episode == null) {
            return;
        }
        this.episode = episode;
        activity.get().description.setText(
                Html.fromHtml(
                        Util.description(episode, activity.get().getString(R.string.no_description)
                        )
                )
        );
    }

    private void configureColorSheme() {

        Decorator.setStatusBarColor((Activity) activity.get().toolbar.getContext(),
                Util.posterTint(episode));

        this.epTint = Util.posterTint(episode);
        this.pureColor = epTint % 0x1000000;
        this.lightBackground = pureColor > 0xff_88_88_88;

        int textColor = lightBackground ? darkColor : lightColor;

        activity.get().tabLayout.setSelectedTabIndicatorColor(textColor);
        activity.get().tabLayout.setTabTextColors(middleColor, lightColor);

        activity.get().toolbar.setTitleTextColor(textColor);
        activity.get().toolbar.setSubtitleTextColor(textColor);
        activity.get().toolbar.setBackgroundColor(epTint);
        activity.get().description.setTextColor(textColor);
        activity.get().descTitle.setTextColor(textColor);
    }

    private int tint() {
        return epTint;
    }


    public void onBackgroundLoaded(Bitmap resource) {
        int averageColorInt = Decorator.getAverageColorInt(resource);

        int tintColor = 0xf0000000 + averageColorInt % 0x1000000;

        Realm.getDefaultInstance().executeTransaction(realm -> Util.storePosterTint(episode, tintColor));

        activity.get().blurArea.setBackgroundColor(tintColor);

        configureColorSheme();
    }

    @Override
    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
        onBackgroundLoaded(resource);
        return false;
    }

}

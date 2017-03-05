package com.iam.oneom.pages.main.EpisodePage;

import android.support.v7.app.AppCompatActivity;

import com.iam.oneom.R;
import com.iam.oneom.core.entities.Util;
import com.iam.oneom.core.entities.model.Episode;

import java.lang.ref.WeakReference;

import butterknife.BindColor;
import butterknife.ButterKnife;

public abstract class EpisodePageNestedChildAdapter {

    private WeakReference<AppCompatActivity> activity;
    private Episode episode;

    @BindColor(R.color.light)
    protected int lightColor;
    @BindColor(R.color.dark)
    protected int darkColor;
    @BindColor(R.color.middle)
    protected int middleColor;
    @BindColor(R.color.unactive_dark)
    protected int unactiveDarkColor;
    @BindColor(R.color.unactive_light)
    protected int unactiveLightColor;


    public EpisodePageNestedChildAdapter(AppCompatActivity activity, Episode episode) {
        this.activity = new WeakReference<>(activity);
        this.episode = episode;
        ButterKnife.bind(this, activity);
    }

    public abstract void onCreate();
    public abstract void configureColorSheme();

    protected void updateEpisode(Episode episode) {
        this.episode = episode;
    }

    protected Episode getEpisode() {
        return episode;
    }

    protected AppCompatActivity getActivity() {
        return activity.get();
    }

    public int getTint() {
        return Util.posterTint(episode);
    }

    public boolean isLightBackground() {
        int pureColor = getTint() % 0x1000000;
        return pureColor > middleColor;
    }

    public int getLightColor() {
        return lightColor;
    }

    public int getDarkColor() {
        return darkColor;
    }

    public int getMiddleColor() {
        return middleColor;
    }

    public int getUnactiveDarkColor() {
        return unactiveDarkColor;
    }

    public int getUnactiveLightColor() {
        return unactiveLightColor;
    }
}

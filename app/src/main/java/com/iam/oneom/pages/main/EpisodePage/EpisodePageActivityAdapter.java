package com.iam.oneom.pages.main.EpisodePage;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.iam.oneom.R;
import com.iam.oneom.core.entities.Util;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.network.Web;
import com.iam.oneom.core.network.response.EpResponse;
import com.iam.oneom.core.rx.EpisodeDataReceivedEvent;
import com.iam.oneom.core.rx.RxBus;
import com.iam.oneom.core.util.Decorator;
import com.iam.oneom.core.util.Time;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EpisodePageActivityAdapter extends EpisodePageNestedChildAdapter
        implements Callback<EpResponse>, RequestListener<String, Bitmap> {

    private Episode episode;

    private WeakReference<EpisodePageActivityNew> activity;

    public EpisodePageActivityAdapter(EpisodePageActivityNew pageActivityNew, Episode episode) {
        super(pageActivityNew, episode);
        activity = new WeakReference<>(pageActivityNew);
        this.episode = episode;
    }

    @Override
    public void onCreate() {



        Web.instance.getEpisode(episode.getId()).enqueue(this);

        configureColorSheme();

        activity.get().toolbar.setTitle(Util.title(episode));
        activity.get().toolbar.setSubtitle(activity.get().getString(R.string.airdate) + ": " + Time.format(episode.getAirdate(), Time.TimeFormat.TEXT));

        activity.get().blurArea.setBackgroundColor(getTint());

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

        setupViewPager(activity.get().pager);
    }

    private void setupViewPager(ViewPager viewPager) {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(EpisodePageFragment.getFragment(episode.getId()), activity.get().getString(R.string.episode).toUpperCase());
        adapter.addFragment(OnlinePageFragment.getFragment(episode.getId()), activity.get().getString(R.string.online).toUpperCase());
        adapter.addFragment(new DummyFragment(), activity.get().getString(R.string.torrent).toUpperCase());
        adapter.addFragment(new DummyFragment(), activity.get().getString(R.string.subtitles).toUpperCase());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

        viewPager.setCurrentItem(0);
        activity.get().tabLayout.setupWithViewPager(viewPager);
    }

    public void onEpisodeGetted(Episode episode) {
        if (episode == null) {
            return;
        }
        this.episode = episode;
    }

    @Override
    public void configureColorSheme() {

        Decorator.setStatusBarColor((Activity) activity.get().toolbar.getContext(), getTint());

        int textColor = isLightBackground() ? darkColor : lightColor;
        int deactivateColor = isLightBackground() ? unactiveDarkColor : unactiveLightColor;

        activity.get().tabLayout.setBackgroundColor(getTint());
        activity.get().tabLayout.setSelectedTabIndicatorColor(textColor);
        activity.get().tabLayout.setTabTextColors(deactivateColor, textColor);

        activity.get().toolbar.setTitleTextColor(textColor);
        activity.get().toolbar.setSubtitleTextColor(textColor);
        activity.get().toolbar.setBackgroundColor(getTint());
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

    @Override
    public void onResponse(Call<EpResponse> call, Response<EpResponse> response) {
        onEpisodeGetted(response.body().getEpisode());
        RxBus.INSTANCE.post(new EpisodeDataReceivedEvent(episode));
    }

    @Override
    public void onFailure(Call<EpResponse> call, Throwable t) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}

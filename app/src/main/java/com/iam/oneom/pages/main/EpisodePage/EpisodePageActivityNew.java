package com.iam.oneom.pages.main.EpisodePage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.iam.oneom.R;
import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.Util;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.network.Web;
import com.iam.oneom.core.network.response.EpResponse;
import com.iam.oneom.core.rx.EpisodeImageTintDefinedEvent;
import com.iam.oneom.core.rx.RxBus;
import com.iam.oneom.core.util.Decorator;
import com.iam.oneom.core.util.Time;
import com.iam.oneom.pages.main.SerialPageActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EpisodePageActivityNew extends AppCompatActivity
        implements Callback<EpResponse>, RequestListener<String, Bitmap> {

    private static final String TAG = EpisodePageActivityNew.class.getSimpleName();
    private static final String EP_ID_EXTRA = "EP_ID_EXTRA";

    Episode episode;

    public static void open(Context context, long epId) {
        Intent intent = new Intent(context, EpisodePageActivityNew.class);
        intent.putExtra(EP_ID_EXTRA, epId);
        context.startActivity(intent);
    }

    @BindView(R.id.poster)
    public ImageView imagePoster;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.bluring_area)
    public FrameLayout blurArea;
    @BindView(R.id.tabs)
    public TabLayout tabLayout;
    @BindView(R.id.pager)
    public ViewPager pager;

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

    @BindDimen(R.dimen.ep_poster_corner_radius)
    public int cornerRadius;

    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.episode_page_activity);
        ButterKnife.bind(this);
        setData();
        if (episode != null) {
            configureViews();
        }
    }

    private void setData() {
        long id = getIntent().getLongExtra(EP_ID_EXTRA, 0);
        episode = DbHelper.where(Episode.class).equalTo("id", id).findFirst();
        Web.instance.getEpisode(id).enqueue(this);
    }

    public void configureViews() {

        configureColorSheme();

        toolbar.setTitle(Util.title(episode));
        toolbar.setSubtitle(getString(R.string.airdate) + ": " + Time.format(episode.getAirdate(), Time.TimeFormat.TEXT));
        toolbar.setOnClickListener(v -> SerialPageActivity.start(this, episode.getSerial().getId()));

        Glide.with(this)
                .load(Util.posterUrl(episode, Decorator.W480))
                .asBitmap()
                .centerCrop()
                .listener(this)
                .into(new BitmapImageViewTarget(imagePoster) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        imagePoster.setImageBitmap(Decorator.fastblur(resource, 1, 50));
                    }
                });

        setupViewPager(pager);
    }

    private void setupViewPager(ViewPager viewPager) {

        if (adapter != null) {
            return;
        }

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(EpisodePageFragment.getFragment(episode.getId()), getString(R.string.episode).toUpperCase());
        adapter.addFragment(OnlinePageFragment.getFragment(episode.getId()), getString(R.string.online).toUpperCase());
        adapter.addFragment(new DummyFragment(), getString(R.string.torrent).toUpperCase());
        adapter.addFragment(new DummyFragment(), getString(R.string.subtitles).toUpperCase());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void configureColorSheme() {

        Decorator.setStatusBarColor((Activity) toolbar.getContext(), Util.posterTint(episode));

        int textColor = isLightBackground() ? darkColor : lightColor;
        int deactivateColor = isLightBackground() ? unactiveDarkColor : unactiveLightColor;

        tabLayout.setBackgroundColor(Util.posterTint(episode));
        tabLayout.setSelectedTabIndicatorColor(textColor);
        tabLayout.setTabTextColors(deactivateColor, textColor);

        toolbar.setTitleTextColor(textColor);
        toolbar.setSubtitleTextColor(textColor);
        toolbar.setBackgroundColor(Util.posterTint(episode));

        blurArea.setBackgroundColor(Util.posterTint(episode));
    }

    @Override
    public void onResponse(Call<EpResponse> call, Response<EpResponse> response) {
        Episode episode = response.body().getEpisode();
        this.episode = episode;
        DbHelper.insert(episode, this::configureViews);
    }

    @Override
    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
        Util.storePosterTint(episode, Decorator.setTransparencyPercent(95, Decorator.getAverageColorInt(resource)));
        configureColorSheme();
        RxBus.INSTANCE.post(new EpisodeImageTintDefinedEvent(Decorator.pureColor(Util.posterTint(episode))));
        return false;
    }

    public boolean isLightBackground() {
        return Decorator.pureColor(Util.posterTint(episode)) > middleColor;
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
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

    @Override
    public void onFailure(Call<EpResponse> call, Throwable t) {

    }

    @Override
    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
        return false;
    }
}

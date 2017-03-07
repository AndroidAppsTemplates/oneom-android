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
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.iam.oneom.R;
import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.Util;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.network.Web;
import com.iam.oneom.core.rx.EpisodeDataReceivedEvent;
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
import rx.Subscription;

public class EpisodePageActivityNew extends AppCompatActivity {

    private static final String TAG = EpisodePageActivityNew.class.getSimpleName();
    private static final String EP_ID_EXTRA = "EP_ID_EXTRA";

    Episode episode;

    Subscription subscription;

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
        configureViews();
    }

    @Override
    public void finish() {
        super.finish();
        subscription.unsubscribe();
    }

    private void setData() {
        long id = getIntent().getLongExtra(EP_ID_EXTRA, 0);
        episode = DbHelper.where(Episode.class).equalTo("id", id).findFirst();
        subscription = Web.instance.getEpisode(id)
                .subscribe(
                        epResponse -> {
                            episode = epResponse.getEpisode();
                            DbHelper.insert(episode, this::configureViews);
                            RxBus.INSTANCE.post(new EpisodeDataReceivedEvent(episode));
                        }
                );
    }

    public void configureViews() {

        if (episode == null) {
            return;
        }

        toolbar.setTitle(Util.title(episode));
        toolbar.setSubtitle(getString(R.string.airdate) + ": " + Time.format(episode.getAirdate(), Time.TimeFormat.TEXT));
        toolbar.setOnClickListener(v -> SerialPageActivity.start(this, episode.getSerial().getId()));

        configureColors();

        Glide.with(this)
                .load(Util.posterUrl(episode, Decorator.W480))
                .asBitmap()
                .centerCrop()
                .into(new BitmapImageViewTarget(imagePoster) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        imagePoster.setImageBitmap(Decorator.fastblur(resource, 1, 50));
                    }
                });

        setupViewPager(pager);
    }

    private void configureColors() {
        Decorator.setStatusBarColor(this, darkColor);
        toolbar.setTitleTextColor(lightColor);
        toolbar.setSubtitleTextColor(middleColor);
        tabLayout.setTabTextColors(unactiveLightColor, lightColor);
        tabLayout.setSelectedTabIndicatorColor(lightColor);
    }

    private void setupViewPager(ViewPager viewPager) {

        if (adapter != null) {
            return;
        }

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(EpisodePageFragment.getFragment(episode.getId()), getString(R.string.episode).toUpperCase());
        adapter.addFragment(OnlinePageFragment.getFragment(episode.getId()), getString(R.string.online).toUpperCase());
        adapter.addFragment(TorrentPageFragment.getFragment(episode.getId()), getString(R.string.torrent).toUpperCase());
        adapter.addFragment(SubtitlesPageFragment.getFragment(episode.getId()), getString(R.string.subtitles).toUpperCase());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);
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
}

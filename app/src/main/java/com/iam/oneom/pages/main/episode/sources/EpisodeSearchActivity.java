package com.iam.oneom.pages.main.episode.sources;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.iam.oneom.R;
import com.iam.oneom.binding.episode.EpisodeViewModel;
import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.databinding.EpisodeSearchSourcesActivityBinding;
import com.iam.oneom.env.ViewPagerAdapter;

public class EpisodeSearchActivity extends AppCompatActivity {

    private static final String EP_ID_EXTRA = "EP_ID_EXTRA";


    public static void start(Context context, long epId) {
        Intent intent = new Intent(context, EpisodeSearchActivity.class);
        intent.putExtra(EP_ID_EXTRA, epId);
        context.startActivity(intent);
    }

    public static void start(View view, long epId) {
        start(view.getContext(), epId);
    }

    private EpisodeViewModel episodeViewModel;
    private EpisodeSearchSourcesActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.episode_search_sources_activity);

        Episode episode = DbHelper
                .where(Episode.class)
                .equalTo("id", getIntent().getLongExtra(EP_ID_EXTRA, 0))
                .findFirst();
        episodeViewModel = new EpisodeViewModel(
                episode
        );

        binding.setVm(episodeViewModel);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(OnlinePageFragment.getFragment(episode.getId()), getString(R.string.online).toUpperCase());
        adapter.addFragment(TorrentPageFragment.getFragment(episode.getId()), getString(R.string.torrent).toUpperCase());
        adapter.addFragment(SubtitlesPageFragment.getFragment(episode.getId()), getString(R.string.subtitles).toUpperCase());
        binding.pager.setAdapter(adapter);
//        binding.tabs.setupWithViewPager(binding.pager);
    }


//    private void setupViewPager(ViewPager viewPager) {
//
//        if (adapter != null) {
//            return;
//        }
//
//        adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(EpisodePageFragment.getFragment(episode.getId()), getString(R.string.episode).toUpperCase());
//        adapter.addFragment(OnlinePageFragment.getFragment(episode.getId()), getString(R.string.online).toUpperCase());
//        adapter.addFragment(TorrentPageFragment.getFragment(episode.getId()), getString(R.string.torrent).toUpperCase());
//        adapter.addFragment(SubtitlesPageFragment.getFragment(episode.getId()), getString(R.string.subtitles).toUpperCase());
//        viewPager.setAdapter(adapter);
//        viewPager.setOffscreenPageLimit(3);
//        viewPager.setCurrentItem(0);
////        tabLayout.setupWithViewPager(viewPager);
//    }

}

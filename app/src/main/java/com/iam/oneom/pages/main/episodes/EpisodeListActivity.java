package com.iam.oneom.pages.main.episodes;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.iam.oneom.R;
import com.iam.oneom.databinding.EpisodesListActivityBinding;
import com.iam.oneom.pages.main.serial.SerialSearchActivity;

public class EpisodeListActivity extends AppCompatActivity
        implements Toolbar.OnMenuItemClickListener {

    EpisodesViewModel episodesViewModel = new EpisodesViewModel(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EpisodesListActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.episodes_list_activity);
        binding.setEpisodesViewModel(episodesViewModel);
        setSupportActionBar(binding.toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        episodesViewModel.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        episodesViewModel.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.episode_list_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                SerialSearchActivity.start(this);
                return true;
        }
        return false;
    }
}
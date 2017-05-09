package com.iam.oneom.pages.main.episodes;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.iam.oneom.R;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.util.Time;
import com.iam.oneom.databinding.EpisodesListActivityBinding;
import com.iam.oneom.pages.mpd.PagingPresenter;
import com.iam.oneom.pages.mpd.View;

import java.util.List;

public class EpisodeListActivity extends AppCompatActivity
        implements Toolbar.OnMenuItemClickListener, View<Episode> {

    public PagingPresenter presenter = new EpisodesListPresenter(this);

    ViewModel viewModel = new ViewModel(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EpisodesListActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.episodes_list_activity);
        binding.setViewModel(viewModel);
        binding.setPresenter(presenter);
        setSupportActionBar(binding.toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.episode_list_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }


    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {
        viewModel.loading.set(false);
    }

    @Override
    public void show(List<Episode> list) {
        viewModel.lastUpdated.set(Time.episodesLastUpdated(this));
        viewModel.items.addAll(list);
    }

    @Override
    public void onItemClick(Episode item) {

    }

    @Override
    public void reportError(Throwable t) {
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_LONG).show();
    }
}
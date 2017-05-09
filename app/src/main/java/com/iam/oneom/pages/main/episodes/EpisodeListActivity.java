package com.iam.oneom.pages.main.episodes;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.iam.oneom.R;
import com.iam.oneom.core.SecureStore;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.util.Decorator;
import com.iam.oneom.core.util.Time;
import com.iam.oneom.env.widget.CircleProgressBar;
import com.iam.oneom.pages.mpd.PagingPresenter;
import com.iam.oneom.pages.mpd.View;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.Date;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

public class EpisodeListActivity extends AppCompatActivity
        implements Toolbar.OnMenuItemClickListener, View<Episode> {

    private static final int EPISODES_IN_ROW = 3;

    @BindView(R.id.progress)
    CircleProgressBar progressBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mainrecycler)
    RecyclerView episodesGrid;
    @BindView(R.id.background)
    ImageView background;
    @BindView(R.id.swipe_refresh_layout)
    SwipyRefreshLayout swipeRefreshLayout;

    @BindDimen(R.dimen.eps_list_spacing)
    int spacing;

    @BindColor(R.color.light)
    protected int lightColor;
    @BindColor(R.color.dark)
    protected int darkColor;
    @BindColor(R.color.middle)
    protected int middleColor;

    private PagingPresenter presenter;

    private EpisodesAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.episodes_list_activity);
        ButterKnife.bind(this);

        presenter = new EpisodesListPresenter(this);

        configureViews();
        configureViewsData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    private void configureViews() {

        swipeRefreshLayout.setOnRefreshListener(direction -> {
            if (direction == SwipyRefreshLayoutDirection.TOP) {
                presenter.refresh(EpisodeListActivity.this);
            } else {
                presenter.loadMore();
            }
        });

        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(lightColor);
        toolbar.setSubtitleTextColor(middleColor);


        episodesGrid.setLayoutManager(new GridLayoutManager(this, EPISODES_IN_ROW));

        toolbar.setOnMenuItemClickListener(this);
        background.setImageBitmap(Decorator.fastblur(BitmapFactory.decodeResource(getResources(), R.drawable.logo_bg, null), 1, 100));

    }

    private void configureViewsData() {
        if (getSupportActionBar() == null) {
            return;
        }

        getSupportActionBar().setTitle(R.string.episodes);
        getSupportActionBar().setSubtitle(getString(R.string.last_updated,
                SecureStore.getEpisodesLastUpdated() == 0 ?
                        getString(R.string.never) : Time.format(new Date(SecureStore.getEpisodesLastUpdated()), "HH:mm, dd MMM yyyy")));

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
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void show(List<Episode> list) {
        if (adapter == null) {
            adapter = new EpisodesAdapter(this, list);
            episodesGrid.setAdapter(adapter);
        } else {
            adapter.addData(list);
        }
        configureViewsData();
    }

    @Override
    public void onItemClick(Episode item) {

    }

    @Override
    public void reportError(Throwable t) {
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_LONG).show();
    }
}
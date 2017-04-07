package com.iam.oneom.pages.main;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iam.oneom.R;
import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.SecureStore;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.rx.RxBus;
import com.iam.oneom.core.rx.UpdateFinishedEvent;
import com.iam.oneom.core.update.Updater;
import com.iam.oneom.core.util.Decorator;
import com.iam.oneom.core.util.Time;
import com.iam.oneom.env.handling.recycler.layoutmanagers.LinearLayoutManager;
import com.iam.oneom.env.widget.CircleProgressBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Sort;

public class EpisodeListActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    private static final String TAG = EpisodeListActivity.class.getSimpleName();

//    ListPopupWindow popupWindow;

    @BindView(R.id.progress)
    CircleProgressBar progressBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mainrecycler)
    RecyclerView episodesGrid;
    @BindView(R.id.background)
    ImageView background;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindDimen(R.dimen.eps_list_spacing)
    int spacing;

    @BindColor(R.color.light)
    protected int lightColor;
    @BindColor(R.color.dark)
    protected int darkColor;
    @BindColor(R.color.middle)
    protected int middleColor;

    List<Episode> episodes;
    Map<Date, List<Episode>> groups;

    RecyclerView.LayoutManager recyclerLayoutManager;
    EpisodeGroupAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.episodes_list_activity);
        ButterKnife.bind(this);
        configureViews();
        configureViewsData();

        RxBus.INSTANCE.register(UpdateFinishedEvent.class,
                updateFinishedEvent -> {
                    hideProgressBar();
                    loadData();
                    configureViewsData();
                });

        loadData();
    }

    private void configureViews() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            Updater.update(this);
        });

        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(lightColor);
        toolbar.setSubtitleTextColor(middleColor);

        toolbar.setOnMenuItemClickListener(this);
        background.setImageBitmap(Decorator.fastblur(BitmapFactory.decodeResource(getResources(), R.drawable.logo_bg, null), 1, 100));

    }

    private void configureViewsData() {
        getSupportActionBar().setTitle(R.string.episodes);
        getSupportActionBar().setSubtitle(getString(R.string.last_updated,
                SecureStore.getEpisodesLastUpdated() == 0 ?
                        getString(R.string.never) : Time.format(new Date(SecureStore.getEpisodesLastUpdated()), "HH:mm, dd MMM yyyy")));

    }

    private void loadData() {

        episodes = DbHelper.where(Episode.class).equalTo("isSheldule", true).findAllSorted("airdate", Sort.DESCENDING);

        groups = new LinkedHashMap<>();
        for (Episode e : episodes) {
            if (e.getAirdate() == null) {
                continue;
            }

            if (groups.get(new Date(e.getAirdate())) == null) {
                groups.put(new Date(e.getAirdate()), new ArrayList<>());
                groups.get(new Date(e.getAirdate())).add(e);
            } else {
                groups.get(new Date(e.getAirdate())).add(e);
            }
        }

        if (!isThereAreEpisodes()) {
            return;
        }

        invalidateRecycler();
    }

    private void invalidateRecycler() {
        recyclerLayoutManager = new LinearLayoutManager(this);
        episodesGrid.setLayoutManager(recyclerLayoutManager);
        adapter = new EpisodeGroupAdapter(groups);
        episodesGrid.setAdapter(adapter);
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

    public boolean isThereAreEpisodes() {
        return  !(episodes == null || episodes.size() == 0);
    }

    class EpisodeGroupAdapter extends RecyclerView.Adapter<EpisodeGroupAdapter.GroupVH> {

        Map<Date, List<Episode>> groups;
        List<Date> keys;


        EpisodeGroupAdapter(Map<Date, List<Episode>> groups) {
            this.groups = groups;
            this.keys = new ArrayList(groups.keySet());
        }

        @Override
        public EpisodeGroupAdapter.GroupVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new GroupVH(
                    LayoutInflater.from(EpisodeListActivity.this).inflate(R.layout.episodes_group_item, parent, false));
        }

        @Override
        public void onBindViewHolder(EpisodeGroupAdapter.GroupVH holder, int position) {
            Date date = keys.get(position);
            holder.onBind(date, groups.get(date));
        }

        @Override
        public int getItemCount() {
            return groups.size();
        }

        class GroupVH extends RecyclerView.ViewHolder {

            @BindView(R.id.recycler)
            RecyclerView recyclerView;
            @BindView(R.id.date)
            TextView date;

            public GroupVH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);

            }

            void onBind(Date date, List<Episode> episodes) {
                this.date.setText(Time.format(date, Time.TimeFormat.TEXT));

                recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 3));
                recyclerView.setAdapter(new EpisodesAdapter(episodes, recyclerView.getContext()));
            }
        }
    }

    class EpisodesAdapter extends RecyclerView.Adapter<EpisodeVH> {

        LayoutInflater inflater;
        List<Episode> episodes = new ArrayList<>();

        public EpisodesAdapter(List<Episode> episodes, Context context) {
            if (context != null) inflater = LayoutInflater.from(context);
            this.episodes = episodes;
        }

        @Override
        public EpisodeVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new EpisodeVH(inflater.inflate(R.layout.episodes_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(EpisodeVH holder, int position) {
            holder.onBind(episodes.get(position));
        }

        @Override
        public int getItemCount() {
            return episodes.size();// + 1;
        }
    }

    private void showProgressBar() {
//        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        swipeRefreshLayout.setRefreshing(false);
//        progressBar.setVisibility(View.INVISIBLE);
    }


//    private void getInitialData() {
//        showProgressBar();
//        Web.instance.getInitialData().enqueue(new Callback<DataConfigResponse>() {
//            @Override
//            public void onResponse(Call<DataConfigResponse> call, Response<DataConfigResponse> response) {
//
//                if (response.code() != 200) {
//                    showError(Web.generateErrorMessage(response));
//                    return;
//                }
//
//                DataConfigResponse request = response.body();
//
//                DbHelper.insertAll(request.getCountries());
//                DbHelper.insertAll(request.getGenres());
//                DbHelper.insertAll(request.getLang());
//                DbHelper.insertAll(request.getNetworks());
//                DbHelper.insertAll(request.getQualities());
//                DbHelper.insertAll(request.getQualityGroups());
//                DbHelper.insertAll(request.getSources());
//                DbHelper.insertAll(request.getStatuses());
//
//                getShelduledEpisodes();
//            }
//
//            @Override
//            public void onFailure(Call<DataConfigResponse> call, Throwable throwable) {
//                showError(throwable.getMessage());
//                throwable.printStackTrace();
//            }
//        });
//    }

//    private void getShelduledEpisodes() {
//
//        Web.instance.getLastEpisodes(null)
//                .subscribe(epsRequest -> {
//
//                    List<Episode> eps = epsRequest.getEps();
//                    List<Episode> futureEps = epsRequest.getFutureEps();
//
//                    for (Episode episode : eps) {
//                        episode.setIsSheldule(true);
//                    }
//                    for (Episode episode : futureEps) {
//                        episode.setIsSheldule(true);
//                    }
//
//                    DbHelper.insertAll(eps);
//                    DbHelper.insertAll(futureEps);
//
//                    SecureStore.setEpisodesLastUpdated(new Date().getTime());
//
//                    RxBus.INSTANCE.post(new EpsReceivedEvent(eps, SecureStore.getEpisodesLastUpdated()));
//
//                    hideProgressBar();
//                }, throwable -> {
//                    showError(throwable.getMessage());
//                    throwable.printStackTrace();
//                });
//    }

//    private void showError(String message) {
//        hideProgressBar();
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
//    }
}
package com.iam.oneom.pages.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.iam.oneom.R;
import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.SecureStore;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.network.Web;
import com.iam.oneom.core.network.response.DataConfigResponse;
import com.iam.oneom.core.rx.EpsReceivedEvent;
import com.iam.oneom.core.rx.RxBus;
import com.iam.oneom.core.util.Decorator;
import com.iam.oneom.env.widget.svg;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private String TAG = SplashActivity.class.getSimpleName();

    @BindView(R.id.logo)
    ImageView imageView;
    @BindView(R.id.refresh)
    FrameLayout refresh;
    @BindView(R.id.error)
    TextView error;

    @OnClick(R.id.refresh)
    public void onRefreshClick(View v) {
        getInitialData(true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        ButterKnife.bind(this);

        Decorator.init(this);

        imageView.setImageDrawable(svg.logo.drawable());

        checkAndDownload();
    }

    private boolean isThereEpisodesInDbCreated() {
        RealmResults<Episode> episodes = DbHelper.where(Episode.class).findAll();

        return (episodes != null && episodes.size() > 0);
    }

    private void checkAndDownload() {
        if (isThereEpisodesInDbCreated()) {
            nextActivity();
        }

        getInitialData(!isThereEpisodesInDbCreated());
    }

    private void getInitialData(boolean needLaunchActivity) {

        hideError();

        Web.instance.getInitialData().enqueue(new Callback<DataConfigResponse>() {
            @Override
            public void onResponse(Call<DataConfigResponse> call, Response<DataConfigResponse> response) {

                if (response.code() != 200) {
                    showError(Web.generateErrorMessage(response));
                    return;
                }

                DataConfigResponse request = response.body();

                DbHelper.insertAll(request.getCountries());
                DbHelper.insertAll(request.getGenres());
                DbHelper.insertAll(request.getLang());
                DbHelper.insertAll(request.getNetworks());
                DbHelper.insertAll(request.getQualities());
                DbHelper.insertAll(request.getQualityGroups());
                DbHelper.insertAll(request.getSources());
                DbHelper.insertAll(request.getStatuses());

                getShelduledEpisodes(needLaunchActivity);
            }

            @Override
            public void onFailure(Call<DataConfigResponse> call, Throwable throwable) {
                showError(throwable.getMessage());
                throwable.printStackTrace();
            }
        });
    }

    private void getShelduledEpisodes(boolean needLaunchActivity) {
        Web.instance.getLastEpisodes(null)
                .subscribe(epsRequest -> {

                    RealmList<Episode> eps = epsRequest.getEps();

                    for (Episode episode : eps) {
                        episode.setIsSheldule(true);
                    }

                    DbHelper.insertAll(eps);

                    if (needLaunchActivity) {
                        nextActivity();
                        return;
                    }

                    SecureStore.setEpisodesLastUpdated(new Date().getTime());

                    RxBus.INSTANCE.post(new EpsReceivedEvent(eps, SecureStore.getEpisodesLastUpdated()));

                }, throwable -> {
                    showError(throwable.getMessage());
                    throwable.printStackTrace();
                });
    }

    private void showError(String errorMessage) {
        error.setText(errorMessage);
        error.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        error.setVisibility(View.GONE);
        refresh.setVisibility(View.GONE);
    }

    private void nextActivity() {
        Intent intent = new Intent(SplashActivity.this, EpisodeListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}

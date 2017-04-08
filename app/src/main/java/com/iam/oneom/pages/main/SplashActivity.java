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
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.rx.RxBus;
import com.iam.oneom.core.rx.UpdateFinishedEvent;
import com.iam.oneom.core.update.Updater;
import com.iam.oneom.core.util.Decorator;
import com.iam.oneom.pages.main.episodes.EpisodeListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;
import rx.Subscription;

public class SplashActivity extends AppCompatActivity {

    private String TAG = SplashActivity.class.getSimpleName();

    @BindView(R.id.logo)
    ImageView imageView;
    @BindView(R.id.refresh)
    FrameLayout refresh;
    @BindView(R.id.error)
    TextView error;

    Subscription subscription;

    @OnClick(R.id.refresh)
    public void onRefreshClick(View v) {
        hideError();
        Updater.update(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        ButterKnife.bind(this);

        Decorator.init(this);

        subscription = RxBus.INSTANCE.register(UpdateFinishedEvent.class, updateFinishedEvent -> {
            if (updateFinishedEvent.getThrowable() != null) {
                showError(updateFinishedEvent.getThrowable().getMessage());
                updateFinishedEvent.getThrowable().printStackTrace();
                if (isThereEpisodesInDbCreated()) {
                    nextActivity();
                }
            } else {
                nextActivity();
            }
        });

        Updater.update(this);

    }

    private boolean isThereEpisodesInDbCreated() {
        RealmResults<Episode> episodes = DbHelper.where(Episode.class).findAll();
        return (episodes != null && episodes.size() > 0);
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
        subscription.unsubscribe();
        Intent intent = new Intent(SplashActivity.this, EpisodeListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}

package com.iam.oneom.pages.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.iam.oneom.R;
import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.network.update.Updater;
import com.iam.oneom.core.rx.RxBus;
import com.iam.oneom.core.rx.event.UpdateFinishedEvent;
import com.iam.oneom.pages.main.episodes.EpisodeListActivity;
import com.iam.oneom.proxy.IProxyFetchInterface;
import com.iam.oneom.proxy.ProxyFetchService;
import com.iam.oneom.util.Decorator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;
import rx.Subscription;

public class SplashActivity extends AppCompatActivity implements ServiceConnection {

    private String TAG = SplashActivity.class.getSimpleName();

    IProxyFetchInterface mIRemoteService;

    // Called when the connection with the service is established
    public void onServiceConnected(ComponentName className, IBinder service) {
        // Following the example above for an AIDL interface,
        // this gets an instance of the IRemoteInterface, which we can use to call on the service
        mIRemoteService = IProxyFetchInterface.Stub.asInterface(service);
    }

    // Called when the connection with the service disconnects unexpectedly
    public void onServiceDisconnected(ComponentName className) {
        Log.e(TAG, "Service has unexpectedly disconnected");
        mIRemoteService = null;
    }

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

//        AsyncTask.execute(() -> {
            startService(new Intent(this, ProxyFetchService.class));
//        });

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

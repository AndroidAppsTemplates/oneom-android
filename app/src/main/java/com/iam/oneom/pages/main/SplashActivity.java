package com.iam.oneom.pages.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.iam.oneom.R;
import com.iam.oneom.core.network.Web;
import com.iam.oneom.core.util.Decorator;
import com.iam.oneom.env.widget.svg;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class SplashActivity extends AppCompatActivity {

    private String TAG = SplashActivity.class.getSimpleName();

    @BindView(R.id.logo)
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        ButterKnife.bind(this);

        Decorator.init(this);

        imageView.setImageDrawable(svg.logo.drawable());

        Web.instance.getLastEpisodes((downloaded, total) ->
                Log.d(TAG, "onProgressUpdate: " + downloaded + "/" + total))
                .subscribe(epsRequest -> {
                    Realm.getDefaultInstance().executeTransaction(realm -> realm.insertOrUpdate(epsRequest.getEps()));
                    Intent intent = new Intent(this, EpisodeListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                });
    }
}

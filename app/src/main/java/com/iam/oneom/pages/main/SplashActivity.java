package com.iam.oneom.pages.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.iam.oneom.R;
import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.network.Web;
import com.iam.oneom.core.network.response.DataConfigResponse;
import com.iam.oneom.core.util.Decorator;
import com.iam.oneom.env.widget.svg;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        getInitialData();
    }

    private void getInitialData() {
        Web.instance.getInitialData().enqueue(new Callback<DataConfigResponse>() {
            @Override
            public void onResponse(Call<DataConfigResponse> call, Response<DataConfigResponse> response) {

                DataConfigResponse request = response.body();

                DbHelper.insertAll(request.getCountries());
                DbHelper.insertAll(request.getGenres());
                DbHelper.insertAll(request.getLang());
                DbHelper.insertAll(request.getNetworks());
                DbHelper.insertAll(request.getQualities());
                DbHelper.insertAll(request.getQualityGroups());
                DbHelper.insertAll(request.getSources());
                DbHelper.insertAll(request.getStatuses());

                Web.instance.getLastEpisodes((downloaded, total) ->
                        Log.d(TAG, "onProgressUpdate: " + downloaded + "/" + total))
                        .subscribe(epsRequest -> {
                            RealmList<Episode> eps = epsRequest.getEps();
                            for (Episode episode : eps) {
                                episode.setIsSheldule(true);
                            }
                            DbHelper.insertAll(eps);
                            nextActivity();
                        }, throwable -> {
                            nextActivity();
                        });
            }

            @Override
            public void onFailure(Call<DataConfigResponse> call, Throwable t) {
                getInitialData();
            }
        });
    }

    private void nextActivity() {
        Intent intent = new Intent(SplashActivity.this, EpisodeListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}

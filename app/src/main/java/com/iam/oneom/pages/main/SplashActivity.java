package com.iam.oneom.pages.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.iam.oneom.R;
import com.iam.oneom.core.entities.model.Quality;
import com.iam.oneom.core.network.Web;
import com.iam.oneom.core.network.request.DataConfigRequest;
import com.iam.oneom.env.widget.svg;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private String TAG = SplashActivity.class.getCanonicalName();

    @BindView(R.id.logo)
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        ButterKnife.bind(this);
        imageView.setImageDrawable(svg.logo.drawable());
        Web.instance.getInitialData().enqueue(new Callback<DataConfigRequest>() {
            @Override
            public void onResponse(Call<DataConfigRequest> call, Response<DataConfigRequest> response) {
                Log.d(TAG, "onResponse: " + response.code());
                Realm realm = Realm.getDefaultInstance();
                for (Quality quality : realm.where(Quality.class).findAll()) {
                }
            }

            @Override
            public void onFailure(Call<DataConfigRequest> call, Throwable t) {

            }
        });
    }
}

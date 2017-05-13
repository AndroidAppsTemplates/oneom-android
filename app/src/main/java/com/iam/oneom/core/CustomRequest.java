package com.iam.oneom.core;

import android.util.Log;

import com.iam.oneom.core.network.ObservableCallback;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by iam on 13.05.17.
 */

public enum CustomRequest {

    instance;

    private static final String TAG = CustomRequest.class.getSimpleName();

    public Observable<String> request(String url) {

        HttpLoggingInterceptor loggingInterceptor =
                new HttpLoggingInterceptor(message -> Log.d(TAG, "Web: " + message));
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();



        Request request = new Request.Builder()
                .url(url)
                .build();

        ObservableCallback responseCallback = new ObservableCallback();
        client.newCall(request).enqueue(responseCallback);
        return responseCallback.getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

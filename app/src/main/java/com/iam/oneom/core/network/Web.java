package com.iam.oneom.core.network;


import android.util.Log;

import com.iam.oneom.core.GsonMapper;
import com.iam.oneom.core.network.response.DataConfigResponse;
import com.iam.oneom.core.network.response.EpResponse;
import com.iam.oneom.core.network.response.EpsDateResponse;
import com.iam.oneom.core.network.response.EpsResponse;
import com.iam.oneom.core.network.response.SerialResponse;
import com.iam.oneom.core.network.response.SerialsSearchResponse;
import com.iam.oneom.core.update.UpdateException;
import com.iam.oneom.core.util.Editor;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public enum Web {

    instance;

    private final String TAG = Web.class.getSimpleName();

    private DownloadProgressListener progressListener;
    private final WebInterface webInterface;
    private OkHttpClient mClient;
    private Retrofit mRetrofit;

    Web() {
        HttpLoggingInterceptor loggingInterceptor =
                new HttpLoggingInterceptor(message -> Log.d(TAG, "Web: " + message));
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        mClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(chain -> {

                    Request original = chain.request();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original
                            .newBuilder()
                            .method(original.method(), original.body());

                    requestBuilder.addHeader("Accept", "application/json");


                    Request request = requestBuilder.build();
                    Response response = chain.proceed(request);

                    return response.newBuilder()
                            .body(new ProgressResponseBody(response.body(), progressListener))
                            .build();
                })
                .addInterceptor(loggingInterceptor)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(WebInterface.HOST)
//                .addConverterFactory(new ProgressResponseBodyConverter())
                .addConverterFactory(GsonConverterFactory.create(GsonMapper.INSTANCE.gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(mClient)
                .build();

        webInterface = mRetrofit.create(WebInterface.class);
    }

    public static String generateErrorMessage(retrofit2.Response response) {
        switch (response.code()) {
            case 500:
                return (response.code() + " Internal server error.");
            default:
                return (response.code() + " " + response.message());
        }
    }

    public Call<DataConfigResponse> getInitialData() {
        return webInterface.getInitialData();
    }

    public DataConfigResponse getInitialDataFlat() throws UpdateException {
        try {
            return webInterface.getInitialData().execute().body();
        } catch (Exception e) {
            throw new UpdateException(e);
        }
    }

    public Observable<SerialsSearchResponse> searchSerials(String searchString) {
        return webInterface.searchSerials(Editor.encodeToUTF8(searchString))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<EpsResponse> getLastEpisodes(DownloadProgressListener progressListener) {
        this.progressListener = progressListener;
        return webInterface.getLastEpisodes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public EpsResponse getLastEpisodesFlat() throws UpdateException {
        try {
            return webInterface.getLastEpisodesFlat().execute().body();
        } catch (Exception e) {
            throw new UpdateException(e);
        }
    }

    public void sendError(Map<String, String> data) {
        try {
            webInterface.sendError(data).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Observable<EpsDateResponse> getLastEpisodesByDate(String start, String end) {
        return webInterface.getEpisodesByDateObservable(start, end)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Observable<SerialResponse> getSerial(long id) {
        return webInterface.getSerial(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Observable<EpResponse> getEpisode(long id) {
        return webInterface.getEpisode(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public EpsDateResponse getDateEpisodesFlat(String start) throws UpdateException {
        try {
            return webInterface.getEpisodesByDate(start, null).execute().body();
        } catch (Exception e) {
            throw new UpdateException(e);
        }
    }
}

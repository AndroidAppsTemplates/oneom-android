package com.iam.oneom.core.network;


import android.util.Log;

import com.iam.oneom.core.GsonMapper;
import com.iam.oneom.core.network.payload.DataConfig;
import com.iam.oneom.core.network.payload.Ep;
import com.iam.oneom.core.network.payload.Eps;
import com.iam.oneom.core.network.payload.EpsByDate;
import com.iam.oneom.core.network.payload.SerialPayload;
import com.iam.oneom.core.network.payload.SerialsSearch;
import com.iam.oneom.core.network.update.UpdateException;
import com.iam.oneom.util.Editor;
import com.iam.oneom.util.Time;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
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

    public Call<DataConfig> getInitialData() {
        return webInterface.getInitialData();
    }

    public DataConfig getInitialDataFlat() throws UpdateException {
        try {
            return webInterface.getInitialData().execute().body();
        } catch (Exception e) {
            throw new UpdateException(e);
        }
    }

    public Observable<SerialsSearch> searchSerials(String searchString) {
        return webInterface.searchSerials(Editor.encodeToUTF8(searchString))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Eps> getLastEpisodes(DownloadProgressListener progressListener) {
        this.progressListener = progressListener;
        return webInterface.getLastEpisodes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Eps getLastEpisodesFlat() throws UpdateException {
        try {
            return webInterface.getLastEpisodesFlat().execute().body();
        } catch (Exception e) {
            throw new UpdateException(e);
        }
    }

    public void sendError(Map<String, String> data) {
        webInterface.sendError(data).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public Observable<EpsByDate> getLastEpisodesByDate(String start, String end) {
        return webInterface.getEpisodesByDateObservable(start, end)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Observable<EpsByDate> getLastEpisodesByDate(long start, long end) {
        return webInterface.getEpisodesByDateObservable(Time.toString(start), Time.toString(end))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Observable<SerialPayload> getSerial(long id) {
        return webInterface.getSerial(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Observable<Ep> getEpisode(long id) {
        return webInterface.getEpisode(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public EpsByDate getDateEpisodesFlat(String start) throws UpdateException {
        try {
            return webInterface.getEpisodesByDate(start, null).execute().body();
        } catch (Exception e) {
            throw new UpdateException(e);
        }
    }
}

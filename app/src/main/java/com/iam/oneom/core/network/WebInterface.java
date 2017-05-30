package com.iam.oneom.core.network;


import com.iam.oneom.core.network.payload.DataConfig;
import com.iam.oneom.core.network.payload.Ep;
import com.iam.oneom.core.network.payload.Eps;
import com.iam.oneom.core.network.payload.EpsByDate;
import com.iam.oneom.core.network.payload.SerialPayload;
import com.iam.oneom.core.network.payload.SerialsSearch;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface WebInterface {

    String HOST = "https://oneom.tk";

    @GET("/data/config")
    Call<DataConfig> getInitialData();

    @GET("/ep")
    Observable<Eps> getLastEpisodes();

    @GET("/ep")
    Call<Eps> getLastEpisodesFlat();

    @GET("search/ep")
    Observable<EpsByDate> getEpisodesByDateObservable(@Query("start") String start, @Query("end") String end);

    @GET("search/ep")
    Call<EpsByDate> getEpisodesByDate(@Query("start") String start, @Query("end") String end);

    @GET("/serial/{id}")
    Observable<SerialPayload> getSerial(@Path("id") long id);

    @GET("/search/serial")
    Observable<SerialsSearch> searchSerials(@Query("title") String searchString);

    @GET("/ep/{id}")
    Observable<Ep> getEpisode(@Path("id") long id);

    @FormUrlEncoded
    @POST("/errors/java")
    Call<ResponseBody> sendError(@FieldMap Map<String, String> data);

}

package com.iam.oneom.core.network;


import com.iam.oneom.core.network.response.DataConfigResponse;
import com.iam.oneom.core.network.response.EpResponse;
import com.iam.oneom.core.network.response.EpsDateResponse;
import com.iam.oneom.core.network.response.EpsResponse;
import com.iam.oneom.core.network.response.SerialResponse;
import com.iam.oneom.core.network.response.SerialsSearchResponse;

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
    Call<DataConfigResponse> getInitialData();

    @GET("/ep")
    Observable<EpsResponse> getLastEpisodes();

    @GET("/ep")
    Call<EpsResponse> getLastEpisodesFlat();

    @GET("search/ep")
    Observable<EpsDateResponse> getEpisodesByDateObservable(@Query("start") String start, @Query("end") String end);

    @GET("search/ep")
    Call<EpsDateResponse> getEpisodesByDate(@Query("start") String start, @Query("end") String end);

    @GET("/serial/{id}")
    Observable<SerialResponse> getSerial(@Path("id") long id);

    @GET("/search/serial")
    Observable<SerialsSearchResponse> searchSerials(@Query("title") String searchString);

    @GET("/ep/{id}")
    Observable<EpResponse> getEpisode(@Path("id") long id);

    @FormUrlEncoded
    @POST("/errors/java")
    Call<ResponseBody> sendError(@FieldMap Map<String, String> data);

}

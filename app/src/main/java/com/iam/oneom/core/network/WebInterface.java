package com.iam.oneom.core.network;


import com.iam.oneom.core.network.request.SerialsSearchRequest;
import com.iam.oneom.core.network.response.DataConfigResponse;
import com.iam.oneom.core.network.response.EpResponse;
import com.iam.oneom.core.network.response.EpsResponse;
import com.iam.oneom.core.network.response.SerialResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface WebInterface {

    String HOST = "https://oneom.tk";

    @GET("/data/config")
    Call<DataConfigResponse> getInitialData();

    @GET("/ep")
    Observable<EpsResponse> getLastEpisodes();


    @GET("/serial/{id}")
    Observable<SerialResponse> getSerial(@Path("id") long id);

    @GET("/serial/search/{searchString}")
    Call<SerialsSearchRequest> searchSerials(@Path("searchString") String searchString);

    @GET("/ep/{id}")
    Observable<EpResponse> getEpisode(@Path("id") long id);
}

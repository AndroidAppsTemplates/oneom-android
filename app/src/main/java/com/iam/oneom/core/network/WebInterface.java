package com.iam.oneom.core.network;


import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.entities.model.Serial;
import com.iam.oneom.core.network.request.DataConfigRequest;
import com.iam.oneom.core.network.request.EpsRequest;
import com.iam.oneom.core.network.request.SerialRequest;
import com.iam.oneom.core.network.request.SerialsSearchRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface WebInterface {

    String HOST = "https://oneom.tk";

    @GET("/data/config")
    Call<DataConfigRequest> getInitialData();

    @GET("/ep")
    Observable<EpsRequest> getLastEpisodes();


    @GET("/serial/{id}")
    Observable<SerialRequest> getSerial(@Path("id") long id);

    @GET("/serial/search/{searchString}")
    Call<SerialsSearchRequest> searchSerials(@Path("searchString") String searchString);

}

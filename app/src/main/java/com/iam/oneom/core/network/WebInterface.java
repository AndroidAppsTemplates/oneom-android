package com.iam.oneom.core.network;


import com.iam.oneom.core.network.request.DataConfigRequest;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WebInterface {

    String HOST = "https://oneom.tk";

    @GET("/data/config")
    Call<DataConfigRequest> getInitialData();

}

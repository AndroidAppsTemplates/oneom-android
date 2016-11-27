package com.iam.oneom.core.network;


import android.util.Log;

import com.iam.oneom.core.GsonMapper;
import com.iam.oneom.core.network.request.DataConfigRequest;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public enum Web {

    instance;

    private final String TAG = Web.class.getSimpleName();

    private final WebInterface webInterface;
    private OkHttpClient mClient;
    private Retrofit mRetrofit;

    public String MOCK_TOKEN = "bearer vA2CLcOkcIB27FgXuTRU_yvIzp-Fck0QLWRqwGNYqGZBqTQylk-uMdK4y1lJeSmb-qQ79VFYmzXEu_8DCO-oK2SBCmTtzG7y__L47wYCG8NLzDuXHxnulBUJLCvaKOJrSokvbY6YQPRbPzZnFpX-Nayy7hvhInfwcQtYDkG0NpxDpxnjxxrNU6tAdX1YSJYpC3nv9xMHdDQxRbSiVAO_N05B32NVOZ-2H8ES6jGYFVAamplS9tizGiWAo4jPimNpMX1m8tl1mDr9Fnz5R--6ancrBVf4SZsGZuC-MRDvqAFgHgr1vdNXsUeIhiIwBPvtpI3zX57Qob6KjDfVbteb8CEPees5dcX1o2UI7vtxTC0";

    Web() {
        HttpLoggingInterceptor loggingInterceptor =
                new HttpLoggingInterceptor(message -> Log.d(TAG, "Web: " + message));
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        mClient = new OkHttpClient.Builder()
//                .addNetworkInterceptor(chain -> {
//                    Response originalResponse = chain.proceed(chain.request());
//                    return originalResponse.newBuilder()
//                            .body(new ProgressResponseBody(originalResponse.body(), progressListener))
//                            .build();
//                })
                .connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(chain -> {

                    Request original = chain.request();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .method(original.method(), original.body());

                    requestBuilder.addHeader("Accept", "application/json");


                    Request request = requestBuilder.build();
                    Response response = chain.proceed(request);

                    return response;
                })
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

    public Call<DataConfigRequest> getInitialData() {
        return webInterface.getInitialData();
    }

}

package com.iam.oneom.core.network;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rx.Observable;
import rx.exceptions.OnErrorThrowable;
import rx.subjects.AsyncSubject;

/**
 * Created by iam on 13.05.17.
 */

public class ObservableCallback implements Callback {

    private final AsyncSubject<String> subject = AsyncSubject.create();


    public Observable<String> getObservable() {
        return subject.asObservable();
    }

    @Override
    public void onFailure(Call call, IOException e) {
        subject.onError(OnErrorThrowable.from(OnErrorThrowable.addValueAsLastCause(e, call.request())));
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        subject.onNext(response.body().string());

        subject.onCompleted();
    }
}
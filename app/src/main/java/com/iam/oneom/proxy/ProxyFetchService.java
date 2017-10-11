package com.iam.oneom.proxy;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;

import rx.Emitter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by iam on 07/10/2017.
 */

public class ProxyFetchService extends Service {

    public static final String TAG = ProxyFetchService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Observable.<Boolean>create(emitter -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "onCreate: ");
            }
        }, Emitter.BackpressureMode.NONE)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IProxyFetchInterface.Stub mBinder = new IProxyFetchInterface.Stub() {
        public int getPid() {
            return Process.myPid();
        }

        public void basicTypes(int anInt, long aLong, boolean aBoolean,
                               float aFloat, double aDouble, String aString) {
            // Does nothing
        }
    };

}

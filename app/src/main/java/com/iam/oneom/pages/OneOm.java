package com.iam.oneom.pages;

import android.app.Application;
import android.util.Log;

import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.util.Device;
import com.iam.oneom.core.util.ErrorHandler;
import com.orhanobut.hawk.Hawk;

public class OneOm extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(this).build();
        Device.init(this);
        DbHelper.init(this);

//        svg.init(getApplicationContext());
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Thread thread, final Throwable ex) {
                Log.d("DEH", "Default exception handler");
                ex.printStackTrace();
                ErrorHandler.handleError(thread, ex);
            }
        });
    }

}

package com.iam.oneom.pages;

import android.app.Application;
import android.content.Context;

import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.util.Device;
import com.iam.oneom.core.util.ErrorHandler;
import com.orhanobut.hawk.Hawk;

public class OneOm extends Application {

    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        Hawk.init(this).build();
        Device.init(this);
        DbHelper.init(this);

        Thread.setDefaultUncaughtExceptionHandler((thread, ex) -> {
            new ErrorHandler().handleError(thread, ex, true);
        });
    }

    public static Context getContext() {
        return context;
    }
}

package com.iam.oneom.pages;

import android.app.Application;

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

        Thread.setDefaultUncaughtExceptionHandler((thread, ex) -> {
            new ErrorHandler().handleError(thread, ex, true);
        });
    }

}

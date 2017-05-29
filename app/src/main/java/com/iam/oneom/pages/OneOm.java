package com.iam.oneom.pages;

import android.app.Activity;
import android.app.Application;

import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.util.Device;
import com.iam.oneom.core.util.ErrorHandler;
import com.orhanobut.hawk.Hawk;

import java.lang.ref.WeakReference;

public class OneOm extends Application {

    private static OneOm app;
    private WeakReference<Activity> mCurrentActivity;


    @Override
    public void onCreate() {
        super.onCreate();

        app = this;

        registerActivityLifecycleCallbacks(new SimpleActivityLifecycleCallbacks() {
            @Override
            public void onActivityResumed(Activity activity) {
                mCurrentActivity = new WeakReference<>(activity);
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (mCurrentActivity != null && mCurrentActivity.get() == activity) {
                    mCurrentActivity = null;
                }
            }
        });

        Hawk.init(this).build();
        Device.init(this);
        DbHelper.init(this);

        Thread.setDefaultUncaughtExceptionHandler((thread, ex) -> {
            new ErrorHandler().handleError(thread, ex, true);
        });
    }

    public Activity getCurrentActivity() {
        return mCurrentActivity.get();
    }

    public static OneOm getContext() {
        return app;
    }
}

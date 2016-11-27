package com.iam.oneom.pages;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.iam.oneom.BuildConfig;
import com.iam.oneom.core.search.torrents.ExtraTorrentsSearchResult;
import com.iam.oneom.core.util.Device;
import com.iam.oneom.core.util.ErrorHandler;
import com.iam.oneom.core.util.Web;
import com.iam.oneom.env.widget.svg;
import com.orhanobut.hawk.Hawk;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

import io.realm.Realm;

public class OneOm extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(this).build();
        Device.init(this);
        Realm.init(this);
        svg.init(getApplicationContext());
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

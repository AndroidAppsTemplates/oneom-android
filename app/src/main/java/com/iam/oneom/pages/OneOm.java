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
import io.realm.RealmConfiguration;

public class OneOm extends Application {

    private static final long SCHEMA_VERSION = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(this).build();
        Device.init(this);
        initRealm();

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

    private void initRealm() {
        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
                .schemaVersion(SCHEMA_VERSION)
                .deleteRealmIfMigrationNeeded()
                .build());
    }
}

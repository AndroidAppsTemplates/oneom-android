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
import com.iam.oneom.core.util.Web;
import com.iam.oneom.env.widget.svg;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

public class OneOm extends Application {

    public static AssetManager assetManager;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        svg.init(getApplicationContext());
        context = getApplicationContext();
        assetManager = getAssets();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Thread thread, final Throwable ex) {
                Log.d("DEH", "Default exception handler");
                ex.printStackTrace();
                handleError(thread, ex);
            }
        });
    }

    public static void handleError(Thread thread, Throwable exception, String... message) {
        ErrorReportAsyncTask task;
        if (message.length > 0 && message.length % 2 == 1) {
            task = new ErrorReportAsyncTask(thread, exception, message);
        } else {
            task = new ErrorReportAsyncTask(thread, exception, "uncaught");
        }
        task.execute();
    }

    public static class ErrorReportAsyncTask extends AsyncTask<String, Void, Void> {

        private Thread thread;
        private Throwable exception;
        private String[] message;

        private String pairs = "";
        private int i = 0;

        public ErrorReportAsyncTask(Thread thread, Throwable exception, String... message) {
            this.thread = thread;
            this.exception = exception;
            this.message = message;
        }

        private void writeCause(Throwable e) throws UnsupportedEncodingException {
            final Throwable cause = e.getCause();
            if (cause == null) return;
            else {
                Log.d("cause", cause.toString());
                pairs = Web.addPairs(pairs, String.format("stack_trace_%03d", i++), "Caused by: " + cause.toString());
                writeStackTraceToData(cause);
                writeCause(cause);
            }
        }

        @Override
        protected void onPreExecute() {
            try {
                pairs = Web.addPairs(pairs, format("version"), BuildConfig.DEBUG ? "DEBUG" : "RELEASE");
                for (int i = 1, l = message.length; i < l; i += 2) {
                    pairs = Web.addPairs(pairs, message[i], message[i + 1]);
                }

                pairs = Web.addPairs(pairs, format("android"), Device.osVersion());
                pairs = Web.addPairs(pairs, format("time"), new Date(System.currentTimeMillis()).toString());
                pairs = Web.addPairs(pairs, format("device_bra"), Device.BRAND);
                pairs = Web.addPairs(pairs, format("device_cpu"), Device.CPU);
                pairs = Web.addPairs(pairs, format("device_typ"), Device.deviceType());
                pairs = Web.addPairs(pairs, format("device_dis"), Device.displayInfo());
                pairs = Web.addPairs(pairs, format("device_mod"), Device.MODEL);
                pairs = Web.addPairs(pairs, format("device_sdk"), Device.SDK);
                pairs = Web.addPairs(pairs, format("device_ram"), Device.ramMegabyes() + "MB RAM");
                pairs = Web.addPairs(pairs, format("thread_name"), thread.getName());
                pairs = Web.addPairs(pairs, format("about"), message[0]);
                pairs = Web.addPairs(pairs, format("error"), exception.toString());
                System.out.println(pairs);
                writeStackTraceToData(exception);
                writeCause(exception);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void writeStackTraceToData(Throwable e) throws UnsupportedEncodingException {
            for (StackTraceElement s : e.getStackTrace()) {
                pairs = Web.addPairs(pairs, String.format("stack_trace_%03d", i++), "\t\tat " + s.toString());
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            Web.POST(Web.url.domain + Web.url.error, pairs);
            return null;
        }

        private String format(String s) {
            StringBuilder space = new StringBuilder();
            for (int i = 0, needs_ = 15 - s.length(); i < needs_; i++) {
                space.append("_");
            }
            return space.append(s).toString();
        }
    }
}

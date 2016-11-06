package com.iam.oneom.core.util;

import android.os.AsyncTask;
import android.util.Log;

import com.iam.oneom.BuildConfig;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class ErrorHandler {

    private ErrorHandler() {}

    public static void handleError(Thread thread, Throwable exception, String... message) {
        ErrorReportAsyncTask task;
        if (message.length > 0 && message.length % 2 == 1) {
            task = new ErrorReportAsyncTask(thread, exception, message);
        } else {
            task = new ErrorReportAsyncTask(thread, exception, "uncaught");
        }
        task.execute();
    }

    private static class ErrorReportAsyncTask extends AsyncTask<String, Void, Void> {

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

                Device device = Device.instance();

                pairs = Web.addPairs(pairs, format("android"), device.OS_VERSION);
                pairs = Web.addPairs(pairs, format("time"), new Date(System.currentTimeMillis()).toString());
                pairs = Web.addPairs(pairs, format("device_bra"), device.BRAND);
                pairs = Web.addPairs(pairs, format("device_cpu"), device.CPU);
                pairs = Web.addPairs(pairs, format("device_typ"), device.DEVICE_TYPE);
                pairs = Web.addPairs(pairs, format("device_dis"), device.DISPLAY_INFO);
                pairs = Web.addPairs(pairs, format("device_mod"), device.MODEL);
                pairs = Web.addPairs(pairs, format("device_sdk"), device.SDK);
                pairs = Web.addPairs(pairs, format("device_ram"), device.RAM_MEGABYTES + "MB RAM");
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

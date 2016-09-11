package com.iam.oneom.core.util;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.iam.oneom.pages.OneOm;

import java.lang.*;

public class Device {
    public static final String SDK = Build.VERSION.SDK;
    public static final String MODEL = Build.MODEL;
    public static final String BRAND = Build.BRAND;
    public static final String CPU = Build.CPU_ABI;

    public static String osVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String osType() {
        return "Android";
    }

    public static String deviceType() {
        Context context = OneOm.context;
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        float scaleFactor = metrics.density;
        float widthDp = widthPixels / scaleFactor;
        float heightDp = heightPixels / scaleFactor;
        float smallestWidth = Math.min(widthDp, heightDp);
        if (smallestWidth > 720 || smallestWidth > 600) {
            return "tablet";
        } else return "phone";

    }

    public static long ramMegabyes() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) OneOm.context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        return mi.totalMem / 1048576L;
    }

    public static String displayInfo() {
        WindowManager wm = (WindowManager) OneOm.context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        int density = metrics.densityDpi;

        double x = Math.pow(width/metrics.xdpi,2);
        double y = Math.pow(height/metrics.ydpi,2);
        double screenInches = Math.sqrt(x+y);

        return width + "x" + height + " " + density + "dpi" + " " + Editor.sexyDouble(screenInches) + "\"";
    }

}

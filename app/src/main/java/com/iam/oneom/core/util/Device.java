package com.iam.oneom.core.util;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class   Device {

    private static volatile Device instance;

    public static Device instance() {
        Device localInstance = instance;
        if (localInstance == null) {
            synchronized (Device.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Device();
                }
            }
        }
        return localInstance;
    }

    public static void init(Context context) {
        instance = instance();
        instance.DEVICE_TYPE = deviceType(context);
        instance.RAM_MEGABYTES = ramMegabyes(context);
        instance.DISPLAY_INFO = displayInfo(context);
    }

    public final String SDK = Build.VERSION.SDK;
    public final String MODEL = Build.MODEL;
    public final String BRAND = Build.BRAND;
    public final String CPU = Build.CPU_ABI;
    public final String OS_VERSION = Build.VERSION.RELEASE;
    public final String OS_TYPE = "Android";
    public String DEVICE_TYPE;
    public String DISPLAY_INFO;
    public long RAM_MEGABYTES;


    public static String deviceType(Context context) {
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

    public static long ramMegabyes(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        return mi.totalMem / 1048576L;
    }

    public static String displayInfo(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
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

package com.iam.oneom.core.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ListPopupWindow;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.iam.oneom.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public final class Decorator {

    private static final String TAG = Decorator.class.getSimpleName();

    private Decorator() {
    }

    public static final int BLACK = 0xFF000000;
    public static final int WHITE = 0xFFFFFFFF;
    public static final int WHITE_TRANSPARENT_80 = 0xCCFFFFFF;
    public static final int WHITE_TRANSPARENT_100 = 0x00FFFFFF;
    public static final int WINDOW_HEADER_STROKE_COLOR = 0x00dddddd;
    public static final int TXTLINKBLUE = 0xFF337ab7;


    public static int statusBarHeight;
    private static int screenWidthPx;
    private static int screenHeightPx;
    private static int appHeight;

    public static int getAverageColorInt(Bitmap bitmap) {

        int redBucket = 0;
        int greenBucket = 0;
        int blueBucket = 0;
        int pixelCount = 0;

        for (int y = 0; y < bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getWidth(); x++) {
                int c = bitmap.getPixel(x, y);

                pixelCount++;
                redBucket += Color.red(c);
                greenBucket += Color.green(c);
                blueBucket += Color.blue(c);
                // does alpha matter?
            }
        }

        int averageColor = Color.rgb(redBucket / pixelCount,
                greenBucket / pixelCount,
                blueBucket / pixelCount);

        return averageColor;
    }

    synchronized public static void init(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidthPx = metrics.widthPixels;
        screenHeightPx = metrics.heightPixels;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
        }
        appHeight = screenHeightPx - statusBarHeight;
    }

    synchronized public static int getScreenWidth() {
        return screenWidthPx;
    }

    synchronized public static int getAppHeight() {
        return appHeight;
    }

    synchronized public static int getScreenHeight() {
        return screenHeightPx;
    }


    synchronized public static int getSizeForTable(int colsCount) {
        return (screenWidthPx / colsCount);
    }

    public static void configurePopup(View anchorView, ListPopupWindow lpw, AdapterView.OnItemClickListener listener, List<String> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(anchorView.getContext(), R.layout.popup_item, data);
        lpw.setAdapter(adapter);
        lpw.setModal(false);
        lpw.setWidth((int) Decorator.dipToPixels(anchorView.getContext(), 200));
        lpw.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        lpw.setAnchorView(anchorView);
        lpw.setOnItemClickListener(listener);
        lpw.show();
    }

    public static void setStatusBarColor(Activity activity, int color) {

        int resColor;

        WeakReference<Resources> resources = new WeakReference<>(activity.getResources());

        int transparentAddition = resources.get().getInteger(R.integer.transparency_addition);
        int darkOffset = resources.get().getInteger(R.integer.dark_offset);

        int operatingColor = color < 0 ?
                (transparentAddition + 0xffffff + color) : (transparentAddition + color);

        if (operatingColor - darkOffset > transparentAddition) {
            resColor = operatingColor - darkOffset;
        } else {
            resColor = transparentAddition;
        }

        Log.d(TAG, "setStatusBarColor: " + Integer.toHexString(operatingColor));
        Log.d(TAG, "setStatusBarColor: " + Integer.toHexString(resColor));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
//            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
            window.setStatusBarColor(resColor);
        }
    }

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }
}
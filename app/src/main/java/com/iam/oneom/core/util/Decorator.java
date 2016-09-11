package com.iam.oneom.core.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.iam.oneom.R;

import java.util.ArrayList;

public final class Decorator {

    private Decorator() {}

    public static final int BLACK =                         0xFF000000;
    public static final int WHITE =                         0xFFFFFFFF;
    public static final int WHITE_TRANSPARENT_65 =          0xA3FFFFFF;
    public static final int WHITE_TRANSPARENT_75 =          0xC0FFFFFF;
    public static final int WHITE_TRANSPARENT_80 =          0xCCFFFFFF;
    public static final int WHITE_TRANSPARENT_100 =         0x00FFFFFF;
    public static final int WINDOW_HEADER_STROKE_COLOR =    0x00dddddd;
    public static final int GRAY_50 =                       0xFF888888;
    public static final int BGGREEN =                       0xFF5cb85c;
    public static final int TXTBLUE =                       0xFF5d7ab7;
    public static final int TXTLINKBLUE =                   0xFF337ab7;


    private static int statusBarHeight;
    private static int widthPixels;
    private static int heightPixels;
    private static int appHeight;

//    public static void configureActionBar(AppCompatActivity activity) {
//        ActionBar actionBar = activity.getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#272821")));
//        }
//    }

    synchronized public static void init(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        widthPixels = metrics.widthPixels;
        heightPixels = metrics.heightPixels;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
        }
        appHeight = heightPixels - statusBarHeight;
    }

    synchronized public static int getScreenWidth() {
        return widthPixels;
    }

    synchronized public static int getAppHeight() {
        return appHeight;
    }

    synchronized public static int getScreenHeight() {
        return heightPixels;
    }


    synchronized public static int getSizeForTable(int colsCount) {
        return (widthPixels / colsCount);
    }

    public static void configurePopup(View anchorView, ListPopupWindow lpw, AdapterView.OnItemClickListener listener, ArrayList<String> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(anchorView.getContext(), R.layout.popup_item, data);
        lpw.setAdapter(adapter);
        lpw.setModal(false);
        lpw.setWidth((int) Decorator.dipToPixels(anchorView.getContext(), 200));
        lpw.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        lpw.setAnchorView(anchorView);
        lpw.setOnItemClickListener(listener);
        lpw.show();
    }

    public static void setRectSize(View v, int w, int h) {
        v.getLayoutParams().height = h;
        v.getLayoutParams().width = w;
    }

    public static void setSquareSize(View v, int a) {
        v.getLayoutParams().height = a;
        v.getLayoutParams().width = a;
    }

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, (float) pixels, (float) pixels, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
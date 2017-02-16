package com.iam.oneom.env.widget.blur;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public class Blurer {

    private BlurArea blurArea;
    private Runnable onComplete;

    private Blurer() {
    }

    public static void applyBlur(BlurArea blurArea) {
        applyBlur(blurArea, null);
    }

    public static void applyBlur(BlurArea blurArea, Runnable onComplete) {

        Blurer blurer = new Blurer();

        blurer.onComplete = onComplete;
        blurer.blurArea = blurArea;

        View blurringView = blurArea.getBlurView();

        if (blurringView == null) {
            return;
        }

        ViewGroup parentView = blurArea.getTargetView();

        blurer.applyBlur(parentView, blurringView);
    }

    private void applyBlur(View parentView, final View blurringView) {

        if (parentView == null) {
            return;
        }

        blurringView.setVisibility(View.VISIBLE);
        parentView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                setChildrenVisibility((ViewGroup) blurringView, View.INVISIBLE);
                blurArea.hideNotIncludedViews();

                parentView.getViewTreeObserver().removeOnPreDrawListener(this);
                boolean drawingCache = parentView.isDrawingCacheEnabled();
                parentView.setDrawingCacheEnabled(true);
                parentView.destroyDrawingCache();
                parentView.buildDrawingCache();
                Bitmap bmp = Bitmap.createBitmap(parentView.getDrawingCache());
                // todo add overlay
                if (!drawingCache) {
                    parentView.setDrawingCacheEnabled(false);
                }

                blurArea.showNotIncludedViews();
                blurringView.setVisibility(View.GONE);
                setChildrenVisibility((ViewGroup) blurringView, View.VISIBLE);

                float blurRadius = 1;

                if (blurArea.getBlurRadius() == blurArea.getBlurStep()) {
                    blurRadius = blurArea.getBlurRadius();
                }

                for (int i = 0; i < blurRadius; i++) {
                    blur(bmp, blurringView.getContext(), 1);
                }

                return true;
            }
        });
    }

    private void blur(Bitmap bkg, Context context, float radius) {

        if (bkg == null) {
            return;
        }

        if (radius > blurArea.getBlurRadius()) {
            return;
        }

        AsyncTask.execute(() -> {

            Bitmap overlay = Bitmap.createBitmap(blurArea.getWidth(), blurArea.getHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(overlay);

            canvas.translate(-blurArea.getLeft(), -blurArea.getTop());
            canvas.drawBitmap(bkg, 0, 0, null);
            RenderScript rs = RenderScript.create(context);
            Allocation overlayAlloc = Allocation.createFromBitmap(
                    rs, overlay);
            ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(
                    rs, overlayAlloc.getElement());
            blur.setInput(overlayAlloc);
            blur.setRadius(radius);
            blur.forEach(overlayAlloc);
            overlayAlloc.copyTo(overlay);
            rs.destroy();
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    blurArea.getBlurView().setBackground(new BitmapDrawable(context.getResources(), overlay));
                    if (blurArea.getBlurStep() > 0) {
                        blur(bkg, context, radius + blurArea.getBlurStep());
                    }
                    if (blurArea.getBlurView().getVisibility() != View.VISIBLE) {
                        blurArea.getBlurView().setVisibility(View.VISIBLE);
                    }
                    if (radius >= blurArea.getBlurRadius()) {
                        if (onComplete != null) {
                            onComplete.run();
                        }
                    }
                }
            });
        });
    }

    private static void setChildrenVisibility(ViewGroup parent, int visibility) {
        for (int i = 0, l = parent.getChildCount(); i < l; i++) {
            View childAt = parent.getChildAt(i);
            if (childAt.getVisibility() != View.GONE) {
                childAt.setVisibility(visibility);
            }
        }
    }
}

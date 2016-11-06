package com.iam.oneom.env.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.iam.oneom.R;
import com.iam.oneom.pages.OneOm;

public enum svg {

    down("idown", R.raw.idown),
    search("isearch", R.raw.isearch);

    svg(String name, int resourse) {
        this.name = name;
        this.resourse = resourse;
    }

    public Bitmap bitmap () {
        Bitmap bitmap;
        Drawable picture = drawable();

        if (drawable().getIntrinsicWidth() <= 0 || drawable().getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(picture.getIntrinsicWidth(), picture.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        picture.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        picture.draw(canvas);
        return bitmap;
    }

    public static svg svgByTag(String tag) {
        if (tag != null) {
            for (svg s : svg.values()) {
                if (s.name.equals(tag)) return s;
            }
        }
        return null;

    }

    public PictureDrawable drawable() {
        SVG svg = null;
        try {
            svg = SVG.getFromResource(context, resourse);
        } catch (SVGParseException e) {
//            OneOm.handleError(Thread.currentThread(), e, "svg.drawable() failed for " + name);
            e.printStackTrace();
        }


        return new PictureDrawable(svg.renderToPicture());
    }

    public static void init(Context context) {
        svg.context = context;
    }

    static Context context;
    String name;
    int resourse;
}

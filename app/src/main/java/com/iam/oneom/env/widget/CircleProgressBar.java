package com.iam.oneom.env.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.iam.oneom.R;
import com.iam.oneom.util.Decorator;

public class CircleProgressBar extends FrameLayout {

    CircleView cv;

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        ((Activity)context).getLayoutInflater().inflate(R.layout.widget_circular_progress_bar, this);
        cv = (CircleView) findViewById(R.id.circle);
        cv.setColor(Decorator.WHITE_TRANSPARENT_80);
        cv.setBorder(0, Decorator.WHITE_TRANSPARENT_100);
        cv.setShadowLayer(0, 0, 0, Decorator.WHITE_TRANSPARENT_100);

    }

}

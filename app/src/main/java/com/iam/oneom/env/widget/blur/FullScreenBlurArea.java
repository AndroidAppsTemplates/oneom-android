package com.iam.oneom.env.widget.blur;

import android.view.View;

import com.iam.oneom.core.util.Decorator;

public class FullScreenBlurArea extends BlurArea {

    public FullScreenBlurArea(View view) {
        super(view);
    }

    @Override
    public int getWidth() {
        return Decorator.getScreenWidth();
    }

    @Override
    public int getHeight() {
        return Decorator.getScreenHeight() - Decorator.statusBarHeight;
    }

    @Override
    public float getBlurRadius() {
        return 25;
    }
}

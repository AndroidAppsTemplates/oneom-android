package com.iam.oneom.env.widget.blur;

import android.view.View;
import android.view.ViewGroup;

public abstract class BlurArea {

    private View view;
    private View[] notIncludedViews;

    public BlurArea(View view) {
        this.view = view;
    }

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract float getBlurRadius();

    public float getBlurStep() {
        return getBlurRadius();
    }

    public int getLeft() {
        return view.getLeft();
    }

    public int getTop() {
        return view.getTop();
    }

    public View getBlurView() {
        return view;
    }

    public void setNotIncludedViews(View... views) {
        notIncludedViews = views;
    }

    public ViewGroup getTargetView() {
        return (ViewGroup) view.getParent();
    }


    public void hideNotIncludedViews() {
        // todo save old state
        if (notIncludedViews != null) {
            for (View view : notIncludedViews) {
                view.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void showNotIncludedViews() {
        // todo restore old state
        if (notIncludedViews != null) {
            for (View view : notIncludedViews) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }
}


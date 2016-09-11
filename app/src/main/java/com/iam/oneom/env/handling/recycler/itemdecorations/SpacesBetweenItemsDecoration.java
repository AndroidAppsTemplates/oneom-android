package com.iam.oneom.env.handling.recycler.itemdecorations;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpacesBetweenItemsDecoration extends RecyclerView.ItemDecoration {

    private final int mVerticalSpaceHeight;
    private final int mHorizontalSpaceWidth;

    public SpacesBetweenItemsDecoration(int size) {
        this.mVerticalSpaceHeight = size;
        this.mHorizontalSpaceWidth = size;
    }

    public SpacesBetweenItemsDecoration(int mVerticalSpaceHeight, int mHorizontalSpaceWidth) {
        this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        this.mHorizontalSpaceWidth = mHorizontalSpaceWidth;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.bottom = mVerticalSpaceHeight / 2;
        outRect.top = mVerticalSpaceHeight / 2;
        outRect.left = mHorizontalSpaceWidth / 2;
        outRect.right = mHorizontalSpaceWidth / 2;
    }


}
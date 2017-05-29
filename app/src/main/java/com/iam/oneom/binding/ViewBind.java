package com.iam.oneom.binding;

import android.databinding.BindingAdapter;
import android.view.View;

/**
 * Created by iam on 21.05.17.
 */

public class ViewBind {

    @BindingAdapter("visible")
    public static void setVisibility(android.view.View v, boolean b) {
        v.setVisibility(b ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    @BindingAdapter("onClick")
    public static void setOnClick(android.view.View v, View.OnClickListener onClickListener) {
        v.setOnClickListener(onClickListener);
    }
}

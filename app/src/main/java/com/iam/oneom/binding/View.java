package com.iam.oneom.binding;

import android.databinding.BindingAdapter;

/**
 * Created by iam on 21.05.17.
 */

public class View {

    @BindingAdapter("visible")
    public static void setVisibility(android.view.View v, boolean b) {
        v.setVisibility(b ? android.view.View.VISIBLE : android.view.View.GONE);
    }
}

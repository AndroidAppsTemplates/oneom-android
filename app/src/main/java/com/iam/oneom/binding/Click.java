package com.iam.oneom.binding;

import android.databinding.BindingAdapter;
import android.view.View;

/**
 * Created by iam on 20.05.17.
 */

public class Click {

    @BindingAdapter("click")
    public static void setClickListener(View v, View.OnClickListener click) {
        v.setOnClickListener(click);
    }
}

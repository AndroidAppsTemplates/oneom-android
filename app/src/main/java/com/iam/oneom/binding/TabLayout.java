package com.iam.oneom.binding;

import android.databinding.BindingAdapter;
import android.support.v4.view.ViewPager;

/**
 * Created by iam on 20.05.17.
 */

public final class TabLayout {

    @BindingAdapter("viewPager")
    public static void setViewPager(android.support.design.widget.TabLayout tabLayout, ViewPager viewPager) {
        tabLayout.setupWithViewPager(viewPager);
    }

}

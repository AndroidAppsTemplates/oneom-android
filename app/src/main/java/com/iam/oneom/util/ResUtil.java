package com.iam.oneom.util;

import android.content.Context;

import com.iam.oneom.pages.OneOm;

/**
 * Created by iam on 20.05.17.
 */

public final class ResUtil {

    private ResUtil() {}

    public static String getString(int resId) {
        return resId == -1 ? "" : OneOm.getContext().getString(resId);
    }

    public static String getString(Context context, int resId) {
        return resId == -1 ? "" : context.getString(resId);
    }

}

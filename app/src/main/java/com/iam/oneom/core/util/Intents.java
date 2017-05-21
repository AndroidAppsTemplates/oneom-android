package com.iam.oneom.core.util;

import android.content.Context;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;

/**
 * Created by iam on 22.05.17.
 */

public final class Intents {

    private Intents() {}

    public static void runWeb(Context context, String url) {
        Uri parse = Uri.parse(url);

        if (parse.getScheme() == null) {
            parse = Uri.parse("https://" + url);
        }

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, parse);
    }
}

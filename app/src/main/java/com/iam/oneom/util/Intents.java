package com.iam.oneom.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.widget.Toast;

import com.iam.oneom.R;

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

    public static void runOnline(Context context, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setType("video/*");
            intent.setData(Uri.parse(url));
            Intent chooser = Intent.createChooser(intent, context.getString(R.string.select_app));
            context.startActivity(chooser);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Toast
                    .makeText(
                            context,
                            R.string.no_apps,
                            Toast.LENGTH_LONG
                    ).show();
        }
    }
}

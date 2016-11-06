package com.iam.oneom.env.widget.text;

import android.content.Context;
import android.graphics.Typeface;

import com.iam.oneom.pages.OneOm;

public enum font {

    font133("regular", "fonts/133.ttf"),
    font133b("bold", "fonts/133b.ttf"),
    font133i("italic", "fonts/133i.ttf"),
    font133l("light", "fonts/133l.ttf"),
    font133sb("semibold", "fonts/133sb.ttf");


    font(String style, String resource) {
        this.style = style;
        this.resource = resource;
    }

    public Typeface typeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(), resource);
    }

    private String resource;
    private String style;
}


package com.iam.oneom.env.widget;

import android.support.v7.widget.Toolbar;

import com.iam.oneom.core.entities.Util;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.util.Time;

public class ToolbarAdapter {

    private Toolbar toolbar;
    private boolean lightBackground;
    private int pureColor;
    private int epTint;

    public ToolbarAdapter(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public void configureToolbarColorSheme(Episode episode) {
        this.epTint = Util.posterTint(episode);
        this.pureColor = epTint % 0x1000000;
        this.lightBackground = pureColor > 0xff_88_88_88;;

        int textColor = lightBackground ? 0xff000000 : 0xffffffff;

        toolbar.setTitleTextColor(textColor);
        toolbar.setSubtitleTextColor(textColor);
        toolbar.setBackgroundColor(epTint);
    }

    public int tint() {
        return epTint;
    }


    public void configureText(Episode episode) {
        toolbar.setTitle(Util.title(episode));
        toolbar.setSubtitle("Aitdate: " + Time.format(episode.getAirdate(), Time.TimeFormat.TEXT));
    }

}

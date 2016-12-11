package com.iam.oneom.core.entities;

import com.iam.oneom.core.entities.model.Episode;

public class Util {

    // Returns formatted episode in season number EQ: S03E24
    public static String episodeInSeason(Episode episode) {
        String s = episode.getSeason();
        String e = episode.getEp();

        if (s == null && e == null) {
            return "";
        }

        if (s == null) {
            return String.format("E%02d", Integer.parseInt(e));
        }

        if (e == null) {
            return String.format("S%02d", Integer.parseInt(s));
        }

        return String.format("S%02dE%02d", Integer.parseInt(s), Integer.parseInt(e));
    }

    public static String qualityTag(Tagged tagged) {
        return tagged.getQuality().getName() + " " + tagged.getLang().getShortName();
    }

}

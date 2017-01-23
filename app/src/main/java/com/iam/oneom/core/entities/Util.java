package com.iam.oneom.core.entities;

import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.entities.model.Lang;
import com.iam.oneom.core.entities.model.QualityGroup;
import com.iam.oneom.core.entities.model.Serial;
import com.iam.oneom.core.entities.model.Source;

import java.util.ArrayList;
import java.util.List;

public class Util {

    // Returns formatted episode in season number EQ: S03E24
    public static String episodeInSeasonString(Episode episode) {
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

    public static String posterUrl(Episode episode) {
        if (episode == null || episode.getSerial() == null) {
            return "";
        }

        return posterUrl(episode.getSerial());
    }

    public static String posterUrl(Serial serial) {

        if (serial == null) {
            return "";
        }

        if (serial.getPoster() == null) {
            return "";
        }

        return serial.getPoster().getOriginal();
    }

    public static String serialStatusName(Serial serial) {
        if (serial == null || serial.getStatus() == null) {
            return "Unknown";
        }
        return serial.getStatus().getName();
    }

    public static List<Episode> episodesForSeasonList(Serial serial, int seasonNumber) {
        List<Episode> res = new ArrayList<>();

        for (Episode e : serial.getEpisode()) {
            if (Integer.parseInt(e.getSeason()) == seasonNumber) res.add(e);
        }
        return res;
    }

    public static int seasonsCountForSerial(Serial serial) {
        int epCount = 0;

        if (serial.getEpisode() == null) {
            return 0;
        }

        for (Episode episode : serial.getEpisode()) {

            if (episode.getSeason() == null) {
                return 0;
            }

            int i = Integer.parseInt(episode.getSeason());

            if (i <= epCount) {
                continue;
            }

            if (i > epCount) {
                epCount = i;
            }
        }

        return epCount;
    }

    // Returns tag text like Web 720p
    public static String qualityTag(Tagged tagged) {


        String name = tagged.getQuality().getName();
        String shortName = tagged.getLang().getShortName();

        if (name == null && shortName == null) {
            return "";
        }

        if (shortName == null) {
            return name;
        }


        if (name == null) {
            return shortName;
        }

        return name + " " + shortName;
    }

    public static int episodesCountForSeason(Serial serial, int selected) {
        return episodesForSeasonList(serial, selected).size();
    }

    public static final List<String> sourceNames(List<Source> sources) {
        List<String> names = new ArrayList<>();
        for (Source source : sources) {
            names.add(source.getName());
        }
        return names;
    }

    public static List<String> langNames(List<Lang> langs) {
        List<String> names = new ArrayList<>();
        for (Lang lang : langs) {
            if (lang.getName() != null) names.add(lang.getName());
        }
        return names;
    }

    public static List<String> langShortNames(List<Lang> langs) {
        List<String> names = new ArrayList<>();
        for (Lang lang : langs) {
            if (lang.getShortName() != null) names.add(lang.getShortName());
        }
        return names;
    }

    public static List<String> qgNames(List<QualityGroup> qualityGroups) {
        List<String> names = new ArrayList<>();
        for (QualityGroup qualityGroup : qualityGroups) {
            names.add(qualityGroup.getName());
        }
        return names;
    }
}

package com.iam.oneom.core.entities;

import android.content.Context;
import android.util.Log;

import com.iam.oneom.R;
import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.entities.model.Lang;
import com.iam.oneom.core.entities.model.Poster;
import com.iam.oneom.core.entities.model.QualityGroup;
import com.iam.oneom.core.entities.model.Serial;
import com.iam.oneom.core.entities.model.Source;
import com.iam.oneom.core.util.Time;
import com.iam.oneom.core.util.Web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Util {

    // Returns formatted episode in season number EQ: S03E24
    public static String episodeInSeasonString(Episode episode) {
        int s = episode.getSeason();
        int e = episode.getEp();

        if (s == 0 && e == 0) {
            return "";
        }

        if (s == 0) {
            return String.format("E%02d", e);
        }

        if (e == 0) {
            return String.format("S%02d", s);
        }

        return String.format("S%02dE%02d", s, e);
    }

    public static String period(Context context, Serial serial) {

        Long start = serial.getStart();
        Long end = serial.getEnd();

        if (start == null) {
            return context.getString(R.string.unknown);
        }

        String addition = "";

        try {
            if (end == null || end == 0) {
                addition = "";
            } else if (Time.getYear(new Date(end)).getTime() < 0) {
                addition = "";
            } else if (Time.getYear(new Date(start)).equals(Time.getYear(new Date(end)))) {
                addition = "";
            } else if (new Date(end).after(new Date())) {
                addition = " - " + context.getString(R.string.till_now);
            } else {
                addition = " - " + Time.format(new Date(end), Time.TimeFormat.YEAR);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Time.format(new Date(start), Time.TimeFormat.YEAR) + addition;
    }

    public static String countries(Serial serial) {
        if (serial.getCountry() == null || serial.getCountry().size() == 0) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0, l = serial.getCountry().size(); i < l; i++) {
            stringBuilder.append(serial.getCountry().get(i).getName());
            if (i != l - 1) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    public static String networks(Serial serial) {
        if (serial.getNetwork() == null || serial.getNetwork().size() == 0) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0, l = serial.getNetwork().size(); i < l; i++) {
            stringBuilder.append(serial.getNetwork().get(i).getName());
            if (i != l - 1) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    public static String title(Episode episode) {
        if (episode == null || episode.getSerial() == null) {
            return "";
        }

        return title(episode.getSerial());
    }

    public static String title(Serial serial) {
        return serial.getTitle();
    }

    public static String posterUrl(Episode episode, String resolution) {
        if (episode == null || episode.getSerial() == null) {
            return "";
        }

        return posterUrl(episode.getSerial(), resolution);
    }

    public static String posterUrl(Serial serial, String resolution) {

        if (serial == null) {
            return "";
        }

        Poster poster = serial.getPoster();


        if (poster == null) {
            return "";
        }

        return Web.url.poster_prefix + poster.getPath() + "/" + resolution + poster.getName();
    }

    public static int posterTint(Episode episode) {
        if (episode == null || episode.getSerial() == null) {
            return 0xffffffff;
        }

        return posterTint(episode.getSerial());
    }

    public static int posterTint(Serial serial) {

        if (serial == null) {
            return 0xffffffff;
        }

        if (serial.getPoster() == null) {
            return 0xffffffff;
        }

        return serial.getPoster().getTintColor();
    }

    public static void storePosterTint(Episode episode, int tintColor) {
        if (episode == null || episode.getSerial() == null) {
            return;
        }

        storePosterTint(episode.getSerial(), tintColor);
    }

    public static void storePosterTint(Serial serial, int tintColor) {
        Log.d("fd", "storePosterTint: ");
        if (serial == null) {
            return;
        }

        if (serial.getPoster() == null) {
            return;
        }

        Serial s = DbHelper.clone(serial);

        s.getPoster().setTintColor(tintColor);

        DbHelper.insert(serial);
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
            if (e.getSeason() == seasonNumber) res.add(e);
        }
        return res;
    }

    public static int seasonsCountForSerial(Serial serial) {
        int epCount = 0;

        if (serial.getEpisode() == null) {
            return 0;
        }

        for (Episode episode : serial.getEpisode()) {

            if (episode.getSeason() == 0) {
                return 0;
            }

            int i = episode.getSeason();

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

    public static String description(Episode episode, String defaultText) {
        if (episode.getDescription() == null || episode.getDescription().size() == 0) {
            return defaultText == null ? "" : defaultText;
        }

        return episode.getDescription().get(0).getBody();
    }

    public static boolean isEmptySource(Source source) {
        return source.getSearchPage() == null || source.getSearchPage().length() == 0;
    }

    public static boolean hasOnlines(Episode episode) {
        return episode.getOnline() != null && episode.getOnline().size() > 0;
    }

    public static int onlinesCount(Episode episode) {
        return episode.getOnline() == null ? 0 : episode.getOnline().size();
    }

    public static boolean hasTorrents(Episode episode) {
        return episode.getTorrent() != null && episode.getTorrent().size() > 0;
    }

    public static int torrentsCount(Episode episode) {
        return episode.getTorrent() == null ? 0 : episode.getTorrent().size();
    }

    public static String searchString(Episode episode) {
        return episode.getSerial().getTitle();
    }
}

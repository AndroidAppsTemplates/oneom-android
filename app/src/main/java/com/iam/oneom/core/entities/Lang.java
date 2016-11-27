package com.iam.oneom.core.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.iam.oneom.core.entities.interfaces.Named;
import com.iam.oneom.pages.OneOm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Lang extends Entity implements Parcelable, Named {

    private static ArrayList<Lang> langs = new ArrayList<>();

    private String openSubtitlesShort;
    private String shortName;
    private String name;

    public Lang(String html, Source.Origin origin) {
        super(html);
        switch (origin) {
            case vodlocker:
                break;
            default:
                break;
        }
    }

    public Lang(JSONObject json) {
        super(json);
        try {
            openSubtitlesShort = json.getString("opensubtitles_short");
            shortName = json.getString("short");
            name = json.getString("name");
        } catch (JSONException e) {
//            OneOm.handleError(Thread.currentThread(), e, "Lang main constructor");
            e.printStackTrace();
        }
    }

    public static Lang getByLang(Lang lang) throws RuntimeException {
        for (Lang l : langs) {
            if (l.equals(lang)) return l;
        }
        throw new RuntimeException("there are no lang in Lang.langs");
    }

    public static Lang getByID(String id) throws RuntimeException {
        for (Lang lang : langs) {
            if (lang.id() == Integer.parseInt(id)) {
                return lang;
            }
        }
        throw new RuntimeException("Lang with id " + id + " doesnt contains in langs list");
    }

    public static Lang getByOrigin(Source.Origin origin) {
        switch (origin) {
            case vodlocker:
            case piratebay:
                for (Lang l : langs) {
                    if (l.shortName().toLowerCase().equals("en")) return l;
                }
                break;
            default:
                return langs.get(0);
        }
        throw new RuntimeException("No rule for lang from this origin");
    }

    public static boolean contains(Lang lang) {
        return langs.contains(lang);
    }

    private static void add(Lang lang) {
        if (!langs.contains(lang)) {
            langs.add(lang);
        }
    }


    public static Lang lang(int position) {
        return langs.get(position);
    }

    public static void init(JSONObject data) throws JSONException {
        JSONArray langs = data.getJSONArray("lang");
        int lLangs = langs.length();
        for (int i = 0; i < lLangs; i++) {
            Lang.add(new Lang(langs.getJSONObject(i)));
        }
    }


    public static ArrayList<String> names() {
        ArrayList<String> names = new ArrayList<>();
        for (Lang l : langs) {
            names.add(l.getName());
        }
        return names;
    }

    protected Lang(Parcel in) {
        super(in);
        openSubtitlesShort = in.readString();
        shortName = in.readString();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(openSubtitlesShort);
        dest.writeString(shortName);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Lang> CREATOR = new Creator<Lang>() {
        @Override
        public Lang createFromParcel(Parcel in) {
            return new Lang(in);
        }

        @Override
        public Lang[] newArray(int size) {
            return new Lang[size];
        }
    };

    public String openSubtitlesShort() { return openSubtitlesShort; }
    public String shortName() { return shortName; }

    @Override
    public String getName() { return name; }
}

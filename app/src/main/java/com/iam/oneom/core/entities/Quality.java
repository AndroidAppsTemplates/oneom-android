package com.iam.oneom.core.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.iam.oneom.core.entities.interfaces.Named;
import com.iam.oneom.pages.OneOm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Quality extends Entity implements Parcelable, Named {

    private String name;
    private String qualityGroupId;
    private QualityGroup qualityGroup;

    private static ArrayList<Quality> qualities = new ArrayList<>();

    public Quality(String html, Source.Origin origin) {
        super(html);
        switch (origin) {
            case vodlocker:
                break;
            default:
                break;
        }
    }

    public Quality(JSONObject data) throws JSONException {
        super(data);
        try {
            name = data.getString("name");
            qualityGroupId = data.getString("quality_group_id");
        } catch (JSONException e) {
            OneOm.handleError(Thread.currentThread(), e, "Quality main constructor");
            e.printStackTrace();
        }
        qualityGroup = QualityGroup.getByID(qualityGroupId);
    }

    private static void add(Quality quality) {
        if (!qualities.contains(quality)) {
            qualities.add(quality);
        }
    }

    public static boolean contains(Quality quality) {
        return qualities.contains(quality);
    }

    public static Quality getByQuality(Quality quality) throws RuntimeException {
        for (Quality q : qualities) {
            if (q.equals(quality)) return q;
        }
        throw new RuntimeException("This quality doesnt contains in qualities list");
    }

    public static Quality getByHtml(String html) {
//        Document doc = Jsoup.parse(html);
        for (Quality q : qualities) {
            if (html.toLowerCase().contains(q.name().toLowerCase())) return q;
        }
        return getByHtml("webdl");
    }

    public static void init(JSONObject data) throws JSONException {
        JSONArray qualities = data.getJSONArray("quality");
        int lQualities = qualities.length();
        for (int i = 0; i < lQualities; i++) {
            Quality.add(new Quality(qualities.getJSONObject(i)));
        }
    }

    protected Quality(Parcel in) {
        super(in);
        name = in.readString();
        qualityGroupId = in.readString();
        qualityGroup = QualityGroup.getByID(qualityGroupId);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(name);
        dest.writeString(qualityGroupId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Quality> CREATOR = new Creator<Quality>() {
        @Override
        public Quality createFromParcel(Parcel in) {
            return new Quality(in);
        }

        @Override
        public Quality[] newArray(int size) {
            return new Quality[size];
        }
    };

    public String name() { return name; }

    public static Quality getByID(String id) throws RuntimeException {
        for (Quality quality : qualities) {
            if (quality.id() == Integer.parseInt(id)) {
                return quality;
            }
        }
        throw new RuntimeException("Quality with id " + id + " doesnt contains in qualities list");
    }
}

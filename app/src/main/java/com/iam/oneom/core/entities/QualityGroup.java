package com.iam.oneom.core.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.iam.oneom.core.entities.interfaces.Named;
import com.iam.oneom.pages.OneOm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QualityGroup extends Entity implements Parcelable, Named {

    private static ArrayList<QualityGroup> groups = new ArrayList<>();
    private String name;

    public QualityGroup(JSONObject jsonObject) {
        super(jsonObject);
        try {
            name = jsonObject.getString("name");
        } catch (JSONException e) {
//            OneOm.handleError(Thread.currentThread(), e, "QualityGroup main constructor");
            e.printStackTrace();
        }
    }

    private static void add(QualityGroup qualityGroup) {
        if (!groups.contains(qualityGroup)) {
            groups.add(qualityGroup);
        }
    }

    public static boolean contains(QualityGroup qualityGroup) {
        return groups.contains(qualityGroup);
    }

    public static QualityGroup group(int position) {
        return groups.get(position);
    }

    public static QualityGroup getByQuality(QualityGroup qualityGroup) throws RuntimeException {
        for (QualityGroup q : groups) {
            if (q.equals(qualityGroup)) return q;
        }
        throw new RuntimeException("This quality group doesnt contains in quality groups list");
    }

    public static QualityGroup getByID(String id) throws RuntimeException {
        for (QualityGroup qg : groups) {
            if (qg.id() == Integer.parseInt(id)) {
                return qg;
            }
        }
        throw new RuntimeException("Quality group with id " + id + " doesnt contains in quality groups list");
    }

    public static ArrayList<String> names() {
        ArrayList<String> names = new ArrayList<>();
        for (QualityGroup qg : groups) {
            names.add(qg.name());
        }
        return names;
    }

    protected QualityGroup(Parcel in) {
        super(in);
        name = in.readString();
    }

    public static QualityGroup qualityGroup(int position) {
        return groups.get(position);
    }

    public static void init(JSONObject data) throws JSONException {
        JSONArray gqualities = data.getJSONArray("gquality");
        int lGQualties = gqualities.length();
        for (int i = 0; i < lGQualties; i++) {
            QualityGroup.add(new QualityGroup(gqualities.getJSONObject(i)));
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QualityGroup> CREATOR = new Creator<QualityGroup>() {
        @Override
        public QualityGroup createFromParcel(Parcel in) {
            return new QualityGroup(in);
        }

        @Override
        public QualityGroup[] newArray(int size) {
            return new QualityGroup[size];
        }
    };

    @Override
    public String name() {
        return name;
    }
}

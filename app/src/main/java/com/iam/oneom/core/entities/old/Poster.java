package com.iam.oneom.core.entities.old;

import android.os.Parcel;
import android.os.Parcelable;

import com.iam.oneom.core.entities.interfaces.Named;

import org.json.JSONException;
import org.json.JSONObject;

public class Poster extends Entity implements Parcelable, Named {

    private String name;
    private String alt;
    private String description;
    private String original;
    private String path;
    private String type;
    private long size;


    public Poster(JSONObject data) {
        super(data);
        try {
            this.name = data.getString("name");
            this.alt = data.getString("alt");
            this.description = data.getString("description");
            this.original = data.getString("original");
            this.path = data.getString("path");
            this.type = data.getString("type");
            this.size = data.getLong("size");
        } catch (JSONException e) {
//            OneOm.handleError(Thread.currentThread(), e, "Poster main constructor");
            e.printStackTrace();
        }
    }

    protected Poster(Parcel in) {
        super(in);
        name = in.readString();
        alt = in.readString();
        description = in.readString();
        original = in.readString();
        path = in.readString();
        type = in.readString();
        size = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(name);
        dest.writeString(alt);
        dest.writeString(description);
        dest.writeString(original);
        dest.writeString(path);
        dest.writeString(type);
        dest.writeLong(size);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Poster> CREATOR = new Creator<Poster>() {
        @Override
        public Poster createFromParcel(Parcel in) {
            return new Poster(in);
        }

        @Override
        public Poster[] newArray(int size) {
            return new Poster[size];
        }
    };

    @Override
    public String getName() {
        return name;
    }

    public String alt() {
        return alt;
    }

    public String description() {
        return description;
    }

    public String original() {
        return original;
    }

    public String path() {
        return path;
    }
}

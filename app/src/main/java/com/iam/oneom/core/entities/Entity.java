package com.iam.oneom.core.entities;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Entity implements Parcelable {

    private String html;
    private String json;
    private int id;

    public Entity(String html) {
        this.html = html;
        this.id = 0;
        this.json = "";
    }

    public Entity(JSONObject json) {
        this.json = json.toString();
        try {
            this.id = json.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        html = "";
    }

    protected Entity(Parcel in) {
        html = in.readString();
        json = in.readString();
        id = in.readInt();
    }

    public static final Creator<Entity> CREATOR = new Creator<Entity>() {
        @Override
        public Entity createFromParcel(Parcel in) {
            return new Entity(in);
        }

        @Override
        public Entity[] newArray(int size) {
            return new Entity[size];
        }
    };

    public String JSON() {
        return json;
    }

    public String HTML() {
        return html;
    }

    public int id() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(html);
        dest.writeString(json);
        dest.writeInt(id);
    }

    @Override
    public boolean equals(Object o) {
        return id == ((Entity)o).id();
    }

    @Override
    public int hashCode() {
        return id;
    }
}

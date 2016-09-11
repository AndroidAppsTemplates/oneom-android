package com.iam.oneom.core.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.iam.oneom.core.util.Time;
import com.iam.oneom.pages.OneOm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Episode extends Entity implements Parcelable {

    private ArrayList<Torrent> torrent;
    private ArrayList<Online> online;
    private int ep;
    private int season;
    private JSONArray subtitle;
    private Serial serial;
    private String serialId;
    private String rait;
    private String title;
    private String vkPostId;
    private String description;
    private String airdate;
    private String createdAt;
    private String deletedAt;
    private String updatedAt;
    private String videoStreamUrl;


    public Episode(JSONObject json) {
        super(json);
        try {
            title = json.getString("title");
            serialId = json.getString("serial_id");
            setSerial(json);
            setTorrents(json);
            setOnlines(json);
            subtitle = json.getJSONArray("subtitle");
            rait = json.getString("rait");
            season = json.getInt("season");
            vkPostId = json.getString("vk_post_id");
            description = json.getString("description");
            ep = json.getInt("ep");
            airdate = json.getString("airdate");
            createdAt = json.getString("created_at");
            updatedAt = json.getString("updated_at");
            deletedAt = json.getString("deleted_at");
        } catch (JSONException e) {
            OneOm.handleError(Thread.currentThread(), e, "Episode main constructor");
            e.printStackTrace();
        }
    }

    private void setOnlines(JSONObject json) throws JSONException {
        online = new ArrayList<>();
        JSONArray jOnline = json.getJSONArray("online");
        if (jOnline.length() > 0) {
            for (int i = 0; i < jOnline.length(); i++) {
                online.add(new Online(jOnline.getJSONObject(i), posterURL()));
            }
        }
    }

    private void setTorrents(JSONObject json) throws JSONException {
        JSONArray jTors = json.getJSONArray("torrent");
        torrent = new ArrayList<>();
        if (jTors.length() > 0) {
            for (int i = 0; i < jTors.length(); i++) {
                torrent.add(new Torrent(jTors.getJSONObject(i)));
            }
        }
    }

    private void setSerial(JSONObject json) throws JSONException {
        if (Serial.containsId(serialId)) {
            this.serial = Serial.getByID(serialId);
        } else {
            this.serial = new Serial(json.getJSONObject("serial"));
            Serial.add(serial);
        }
    }

    protected Episode(Parcel in) {
        super(in);
        torrent = in.createTypedArrayList(Torrent.CREATOR);
        online = in.createTypedArrayList(Online.CREATOR);
        ep = in.readInt();
        season = in.readInt();
        serial = in.readParcelable(Serial.class.getClassLoader());
        rait = in.readString();
        title = in.readString();
        vkPostId = in.readString();
        description = in.readString();
        airdate = in.readString();
        createdAt = in.readString();
        deletedAt = in.readString();
        updatedAt = in.readString();
        videoStreamUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(torrent);
        dest.writeTypedList(online);
        dest.writeInt(ep);
        dest.writeInt(season);
        dest.writeParcelable(serial, flags);
        dest.writeString(rait);
        dest.writeString(title);
        dest.writeString(vkPostId);
        dest.writeString(description);
        dest.writeString(airdate);
        dest.writeString(createdAt);
        dest.writeString(deletedAt);
        dest.writeString(updatedAt);
        dest.writeString(videoStreamUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Episode> CREATOR = new Creator<Episode>() {
        @Override
        public Episode createFromParcel(Parcel in) {
            return new Episode(in);
        }

        @Override
        public Episode[] newArray(int size) {
            return new Episode[size];
        }
    };

    public ArrayList<Torrent> torrent() { return torrent; }
    public ArrayList<Online> online() { return online; }
    public JSONArray subtitle() { return subtitle; }
    public Serial serial() { return serial; }
    public String rait() { return rait; }
    public String title() { return title; }
    public int season() { return season; }
    public String vkPostId() { return vkPostId; }
    public String description() { return description; }
    public int ep() { return ep; }
    public String getVideoStreamUrl() { return videoStreamUrl; }
    public void setVideoStreamUrl(String url) { this.videoStreamUrl = url; }
    public String airdate() {
        return "Airdate: " + Time.goodLookingEnglishDate(this.airdate, Time.TimeFormat.IDN);
    }
    public String createdAt() { return createdAt; }
    public String deletedAt() { return deletedAt; }
    public String updatedAt() { return updatedAt; }

    public String episodeInSeason() {
        return String.format("S%02dE%02d", season, ep);
    }

    public String posterURL() {
        Poster poster = serial.poster();
        if (poster != null) {
            return poster.original();
        } else {
            return "null";
        }
    }
}

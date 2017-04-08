package com.iam.oneom.core.entities.model;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.iam.oneom.core.jsonadapter.DateLongAdapter;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Episode extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    private long id;

    @SerializedName("torrent")
    private RealmList<Torrent> torrent;
    @SerializedName("online")
    private RealmList<Online> online;
    @SerializedName("subtitle")
    private RealmList<Subtitle> subtitle;
    @SerializedName("description")
    private RealmList<Description> description;
    @SerializedName("serial")
    private Serial serial;
    @SerializedName("ep")
    private String ep;
    @SerializedName("season")
    private String season;
    @SerializedName("rait")
    private String rait;
    @SerializedName("title")
    private String title;
    @SerializedName("fb_post_id")
    private String fbPostId;
    @JsonAdapter(DateLongAdapter.class)
    @SerializedName("airdate")
    private Long airdate;
    @JsonAdapter(DateLongAdapter.class)
    @SerializedName("updated_at")
    private long updatedAt;
    @SerializedName("is_schedule")
    private boolean isSheldule;

    public long getId() {
        return id;
    }

    public RealmList<Torrent> getTorrent() {
        return torrent;
    }

    public RealmList<Online> getOnline() {
        return online;
    }

    public RealmList<Subtitle> getSubtitle() {
        return subtitle;
    }

    public RealmList<Description> getDescription() {
        return description;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public Serial getSerial() {
        return serial;
    }

    public String getEp() {
        return ep;
    }

    public String getSeason() {
        return season;
    }

    public String getRait() {
        return rait;
    }

    public String getTitle() {
        return title;
    }

    public Long getAirdate() {
        return airdate;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTorrent(RealmList<Torrent> torrent) {
        this.torrent = torrent;
    }

    public void setOnline(RealmList<Online> online) {
        this.online = online;
    }

    public void setEp(String ep) {
        this.ep = ep;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public void setSubtitle(RealmList<Subtitle> subtitle) {
        this.subtitle = subtitle;
    }

    public void setSerial(Serial serial) {
        this.serial = serial;
    }

    public void setRait(String rait) {
        this.rait = rait;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(RealmList<Description> description) {
        this.description = description;
    }

    public void setIsSheldule(boolean isSheldule) {
        this.isSheldule = isSheldule;
    }
}

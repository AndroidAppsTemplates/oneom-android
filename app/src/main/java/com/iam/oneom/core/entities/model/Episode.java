package com.iam.oneom.core.entities.model;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.iam.oneom.core.entities.DbEntity;
import com.iam.oneom.core.entities.ScheduleItem;
import com.iam.oneom.core.jsonadapter.DateLongAdapter;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Episode extends RealmObject implements DbEntity, ScheduleItem {

    public static final String AIRDATE = "airdate";
    public static final String TITLE = "title";
    public static final String UPDATED_AT = "updatedAt";
    public static final String IS_SCHEDULE = "isSchedule";
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
    private int ep;
    @SerializedName("season")
    private int season;
    @SerializedName("rait")
    private String rait;
    @SerializedName("serial_id")
    private long serialId;
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
    private boolean isSchedule;

    public long getSerialId() {
        return serialId;
    }

    public int getEp() {
        return ep;
    }

    public void setEp(int ep) {
        this.ep = ep;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public String getFbPostId() {
        return fbPostId;
    }

    public boolean isSheldule() {
        return isSchedule;
    }

    public void setFbPostId(String fbPostId) {
        this.fbPostId = fbPostId;
    }

    public void setAirdate(Long airdate) {
        this.airdate = airdate;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setSheldule(boolean sheldule) {
        isSchedule = sheldule;
    }

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

    public void setIsSheldule(boolean isSchedule) {
        this.isSchedule = isSchedule;
    }

    @Override
    public long getScheduleTime() {
        return airdate;
    }
}

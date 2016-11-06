package com.iam.oneom.core.entities.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Episode extends RealmObject {

    @PrimaryKey
    private long id;

    private RealmList<Torrent> torrent;
    private RealmList<Online> online;
    private int ep;
    private int season;
    private Subtitle subtitle;
    private Serial serial;
    private String rait;
    private String title;
    private String vkPostId;
    private String description;
    private String airdate;
    private String videoStreamUrl;


    public void setId(long id) {
        this.id = id;
    }

    public void setTorrent(RealmList<Torrent> torrent) {
        this.torrent = torrent;
    }

    public void setOnline(RealmList<Online> online) {
        this.online = online;
    }

    public void setEp(int ep) {
        this.ep = ep;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public void setSubtitle(Subtitle subtitle) {
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

    public void setVkPostId(String vkPostId) {
        this.vkPostId = vkPostId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAirdate(String airdate) {
        this.airdate = airdate;
    }

    public void setVideoStreamUrl(String videoStreamUrl) {
        this.videoStreamUrl = videoStreamUrl;
    }
}

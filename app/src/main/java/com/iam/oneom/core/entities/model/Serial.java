package com.iam.oneom.core.entities.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Serial extends RealmObject {
    @PrimaryKey
    private long id;
    private Poster poster;
    private RealmList<Genre> genre;
    private RealmList<Country> country;
    private RealmList<Network> network;
    private RealmList<Episode> episode;
    private RealmList<Description> description;
    private Status status;
    private String title;
    private int hide;
    private int tvrageId;
    private int tvmazeId;
    private int mdbId;
    private int tvdbId;
    private int vkGroupId;
    private String imbbId;
    private String imdbRating;
    private int runtime;
    private Date start; // "1950-07-04",
    private Date end; // "1950-07-04",
    private String airtime;
    private String airday;
    private String timezone;
    private String vkGroupUpdate;

    public void setId(long id) {
        this.id = id;
    }

    public void setAirday(String airday) {
        this.airday = airday;
    }

    public void setAirtime(String airtime) {
        this.airtime = airtime;
    }

    public void setCountry(RealmList<Country> country) {
        this.country = country;
    }

    public void setDescription(RealmList<Description> description) {
        this.description = description;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setEpisode(RealmList<Episode> episode) {
        this.episode = episode;
    }

    public void setGenre(RealmList<Genre> genre) {
        this.genre = genre;
    }

    public void setHide(int hide) {
        this.hide = hide;
    }

    public void setImbbId(String imbbId) {
        this.imbbId = imbbId;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public void setMdbId(int mdbId) {
        this.mdbId = mdbId;
    }

    public void setNetwork(RealmList<Network> network) {
        this.network = network;
    }

    public void setPoster(Poster poster) {
        this.poster = poster;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTvdbId(int tvdbId) {
        this.tvdbId = tvdbId;
    }

    public void setTvmazeId(int tvmazeId) {
        this.tvmazeId = tvmazeId;
    }

    public void setTvrageId(int tvrageId) {
        this.tvrageId = tvrageId;
    }

    public void setVkGroupId(int vkGroupId) {
        this.vkGroupId = vkGroupId;
    }

    public void setVkGroupUpdate(String vkGroupUpdate) {
        this.vkGroupUpdate = vkGroupUpdate;
    }
}

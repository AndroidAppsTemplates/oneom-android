package com.iam.oneom.core.entities.model;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.iam.oneom.core.jsonadapter.DateLongAdapter;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Serial extends RealmObject {
    @PrimaryKey
    @SerializedName("id")
    private long id;
    @SerializedName("poster")
    private Poster poster;
    @SerializedName("genre")
    private RealmList<Genre> genre;
    @SerializedName("country")
    private RealmList<Country> country;
    @SerializedName("network")
    private RealmList<Network> network;
    @SerializedName("episode")
    private RealmList<Episode> episode;
    @SerializedName("description")
    private RealmList<Description> description;
    @SerializedName("status")
    private Status status;
    @SerializedName("title")
    private String title;
    @SerializedName("hide")
    private int hide;
    @SerializedName("tvrage_id")
    private int tvrageId;
    @SerializedName("tvmaze_id")
    private int tvmazeId;
    @SerializedName("mdb_id")
    private int mdbId;
    @SerializedName("tvdb_id")
    private int tvdbId;
    @SerializedName("imdb_id")
    private String imbbId;
    @SerializedName("imdb_rating")
    private String imdbRating;
    @SerializedName("runtime")
    private int runtime;
    @JsonAdapter(DateLongAdapter.class)
    @SerializedName("start")
    private Long start; // "1950-07-04",
    @JsonAdapter(DateLongAdapter.class)
    @SerializedName("end")
    private Long end; // "1950-07-04",
    @SerializedName("airtime")
    private String airtime;
    @SerializedName("airday")
    private String airday;
    @SerializedName("timezone")
    private String timezone;


    public long getId() {
        return id;
    }

    public Poster getPoster() {
        return poster;
    }

    public RealmList<Genre> getGenre() {
        return genre;
    }

    public RealmList<Country> getCountry() {
        return country;
    }

    public RealmList<Network> getNetwork() {
        return network;
    }

    public RealmList<Episode> getEpisode() {
        return episode;
    }

    public RealmList<Description> getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public int getHide() {
        return hide;
    }

    public int getTvrageId() {
        return tvrageId;
    }

    public int getTvmazeId() {
        return tvmazeId;
    }

    public int getMdbId() {
        return mdbId;
    }

    public int getTvdbId() {
        return tvdbId;
    }

//    public int getVkGroupId() {
//        return vkGroupId;
//    }

    public String getImbbId() {
        return imbbId;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public int getRuntime() {
        return runtime;
    }

    public Long getStart() {
        return start;
    }

    public Long getEnd() {
        return end;
    }

    public String getAirtime() {
        return airtime;
    }

    public String getAirday() {
        return airday;
    }

    public String getTimezone() {
        return timezone;
    }

//    public String getVkGroupUpdate() {
//        return vkGroupUpdate;
//    }
//
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

//    public void setEnd(Date end) {
//        this.end = end;
//    }

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

//    public void setStart(Date start) {
//        this.start = start;
//    }

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

//    public void setVkGroupId(int vkGroupId) {
//        this.vkGroupId = vkGroupId;
//    }

//    public void setVkGroupUpdate(String vkGroupUpdate) {
//        this.vkGroupUpdate = vkGroupUpdate;
//    }
}

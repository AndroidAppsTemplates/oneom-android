package com.iam.oneom.core.entities.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Serial extends RealmObject {
    @PrimaryKey
    private int id;
    private String title;
    private int hide;
    private int tvrageId;
    private int tvmazeId;
    private int mdbId;
    private int tvdbId;
    private int vkGroupId;
    private String imbbId;
    private String imdbRating;
    private Poster poster;
    private int runtime;
    private Date start; // "1950-07-04",
    private Date end; // "1950-07-04",
    private String airtime;
    private String airday;
    private String timezone;
    private String vkGroupUpdate;
    private Status status;
    private RealmList<Genre> genre;
    private RealmList<Country> country;
    private RealmList<Network> network;
    private RealmList<Episode> episode;
    private RealmList<Description> description;
}

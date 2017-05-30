package com.iam.oneom.core.entities.model;

import com.google.gson.annotations.SerializedName;
import com.iam.oneom.core.entities.interfaces.DbEntity;
import com.iam.oneom.core.entities.interfaces.Named;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Lang extends RealmObject implements Named, DbEntity {

    @PrimaryKey
    @SerializedName("id")
    private long id;
    @SerializedName("opensubtitles_short")
    private String openSubtitlesShort;
    @SerializedName("short")
    private String shortName;
    @SerializedName("name")
    private String name;

    @Override
    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public String getOpenSubtitlesShort() {
        return openSubtitlesShort;
    }

    public String getShortName() {
        return shortName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setOpenSubtitlesShort(String openSubtitlesShort) {
        this.openSubtitlesShort = openSubtitlesShort;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}

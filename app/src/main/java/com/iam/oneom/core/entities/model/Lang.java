package com.iam.oneom.core.entities.model;

import com.iam.oneom.core.entities.interfaces.Named;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Lang extends RealmObject implements Named {

    @PrimaryKey
    private long id;
    private String openSubtitlesShort;
    private String shortName;
    private String name;

    @Override
    public String name() {
        return name;
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

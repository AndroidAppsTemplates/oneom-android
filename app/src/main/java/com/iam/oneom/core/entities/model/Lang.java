package com.iam.oneom.core.entities.model;

import com.iam.oneom.core.entities.interfaces.Named;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Lang extends RealmObject implements Named {

    @PrimaryKey
    private int id;
    private String openSubtitlesShort;
    private String shortName;
    private String name;

    @Override
    public String name() {
        return name;
    }

}

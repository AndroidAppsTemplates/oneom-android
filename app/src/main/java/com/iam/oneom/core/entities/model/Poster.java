package com.iam.oneom.core.entities.model;

import com.iam.oneom.core.entities.interfaces.Named;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Poster extends RealmObject implements Named {

    @PrimaryKey
    private int id;
    private String name;
    private String alt;
    private String description;
    private String original;
    private String path;
    private String type;
    private long size;

    @Override
    public String name() {
        return name;
    }

}

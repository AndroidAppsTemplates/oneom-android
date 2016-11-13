package com.iam.oneom.core.entities.model;

import com.iam.oneom.core.entities.interfaces.Named;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Poster extends RealmObject implements Named {

    @PrimaryKey
    private long id;
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

    public void setId(long id) {
        this.id = id;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setType(String type) {
        this.type = type;
    }
}
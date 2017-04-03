package com.iam.oneom.core.entities.model;

import com.google.gson.annotations.SerializedName;
import com.iam.oneom.core.entities.interfaces.Named;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Poster extends RealmObject implements Named {

    @PrimaryKey
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("alt")
    private String alt;
    @SerializedName("description")
    private String description;
    @SerializedName("original")
    private String original;
    @SerializedName("path")
    private String path;
    @SerializedName("type")
    private String type;
    @SerializedName("size")
    private long size;

    // defined
    private int tintColor = 0xffffffff;


    public int getTintColor() {
        return tintColor;
    }

    public void setTintColor(int tintColor) {
        this.tintColor = tintColor;
    }

    @Override
    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public String getAlt() {
        return alt;
    }

    public String getDescription() {
        return description;
    }

    public String getOriginal() {
        return original;
    }

    public String getPath() {
        return path;
    }

    public String getType() {
        return type;
    }

    public long getSize() {
        return size;
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

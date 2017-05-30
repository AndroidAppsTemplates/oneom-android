package com.iam.oneom.core.entities.model;

import com.google.gson.annotations.SerializedName;
import com.iam.oneom.core.entities.interfaces.DbEntity;
import com.iam.oneom.core.entities.interfaces.Named;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Status extends RealmObject implements Named, DbEntity {

    @PrimaryKey
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

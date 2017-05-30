package com.iam.oneom.core.entities.model;

import com.google.gson.annotations.SerializedName;
import com.iam.oneom.core.entities.interfaces.DbEntity;
import com.iam.oneom.core.entities.interfaces.Named;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class QualityGroup extends RealmObject implements Named, DbEntity {

    @PrimaryKey
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;

    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}

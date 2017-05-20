package com.iam.oneom.core.entities.model;

import com.google.gson.annotations.SerializedName;
import com.iam.oneom.core.entities.DbEntity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by iam on 03.04.17.
 */

public class Sex extends RealmObject implements DbEntity {

    @SerializedName("id")
    @PrimaryKey
    private long id;
    @SerializedName("name")
    private String name;

    @Override
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

package com.iam.oneom.core.entities.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by iam on 03.04.17.
 */

public class Sex extends RealmObject {

    @SerializedName("id")
    @PrimaryKey
    private long id;
    @SerializedName("name")
    private String name;
}

package com.iam.oneom.core.entities.model;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.iam.oneom.core.entities.interfaces.Named;
import com.iam.oneom.core.jsonadapter.CountryAdapter;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

//TODO after country
public class Network extends RealmObject implements Named {

    @PrimaryKey
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @JsonAdapter(CountryAdapter.class)
    @SerializedName("country_id")
    private Country country;

    public Country getCountryId() {
        return country;
    }


    @Override
    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

}
package com.iam.oneom.core.entities.model;

import com.iam.oneom.core.entities.interfaces.Named;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Network extends RealmObject implements Named {

    @PrimaryKey
    private int id;
    private String name;
    private Country country;
    private String countryId;

    @Override
    public String name() {
        return name;
    }

}

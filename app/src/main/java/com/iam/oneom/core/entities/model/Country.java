package com.iam.oneom.core.entities.model;


import com.iam.oneom.core.entities.interfaces.Named;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Country extends RealmObject implements Named {

    @PrimaryKey
    private long id;
    private String name;

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    public long id() {
        return id;
    }
}
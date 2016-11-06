package com.iam.oneom.core.entities.model;

import com.iam.oneom.core.entities.interfaces.Named;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Status extends RealmObject implements Named {

    @PrimaryKey
    private int id;
    private String name;

    @Override
    public String name() {
        return name;
    }
}

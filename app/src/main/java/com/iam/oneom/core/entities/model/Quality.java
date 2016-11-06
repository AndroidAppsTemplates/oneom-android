package com.iam.oneom.core.entities.model;

import com.iam.oneom.core.entities.interfaces.Named;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Quality extends RealmObject implements Named {

    @PrimaryKey
    private int id;
    private String name;
    private String qualityGroupId;
    private QualityGroup qualityGroup;

    @Override
    public String name() {
        return name;
    }

}

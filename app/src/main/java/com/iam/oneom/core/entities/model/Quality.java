package com.iam.oneom.core.entities.model;

import com.iam.oneom.core.entities.interfaces.Named;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Quality extends RealmObject implements Named {

    @PrimaryKey
    private long id;
    private String name;
    private long qualityGroupId;

    public long getId() {
        return id;
    }

    public long getQualityGroupId() {
        return qualityGroupId;
    }

    public void setQualityGroupId(long qualityGroupId) {
        this.qualityGroupId = qualityGroupId;
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

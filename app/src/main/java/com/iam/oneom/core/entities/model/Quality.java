package com.iam.oneom.core.entities.model;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.iam.oneom.core.entities.DbEntity;
import com.iam.oneom.core.entities.interfaces.Named;
import com.iam.oneom.core.jsonadapter.QualityGroupAdapter;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Quality extends RealmObject implements Named, DbEntity {

    @PrimaryKey
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @JsonAdapter(QualityGroupAdapter.class)
    @SerializedName("quality_group_id")
    private QualityGroup qualityGroup;

    public long getId() {
        return id;
    }

    public QualityGroup getQualityGroupId() {
        return qualityGroup;
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

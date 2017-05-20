package com.iam.oneom.core.entities.model;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.iam.oneom.core.entities.DbEntity;
import com.iam.oneom.core.jsonadapter.LangAdapter;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Description extends RealmObject implements DbEntity {

    @PrimaryKey
    @SerializedName("id")
    private long id;
    @SerializedName("assoc_id")
    private long assocId;
    @SerializedName("assoc_type")
    private String assocType;
    @SerializedName("body")
    private String body;
    @SerializedName("type_id")
    private long typeId;
    // TODO
    @JsonAdapter(LangAdapter.class)
    @SerializedName("lang_id")
    private Lang lang;

    public Lang getLang() {
        return lang;
    }

    public String getAssocType() {
        return assocType;
    }

    public void setAssocType(String assocType) {
        this.assocType = assocType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAssocId(long assocId) {
        this.assocId = assocId;
    }


    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public long getId() {
        return id;
    }

    public long getAssocId() {
        return assocId;
    }


    public long getTypeId() {
        return typeId;
    }

}

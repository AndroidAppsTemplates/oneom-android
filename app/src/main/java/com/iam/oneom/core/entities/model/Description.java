package com.iam.oneom.core.entities.model;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Description extends RealmObject {

    @PrimaryKey
    private long id;
    private long assocId;
    private String assocType;
    private String body;
    private long typeId;
    private long lang;

    public long getLang() {
        return lang;
    }

    public void setLangId(long lang) {
        this.lang = lang;
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

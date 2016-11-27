package com.iam.oneom.core.entities.model;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Description extends RealmObject {

    @PrimaryKey
    private long id;
    private long assocId;
    private long assocType;
    private long typeId;
    private Lang lang;

    public void setId(long id) {
        this.id = id;
    }

    public void setAssocId(long assocId) {
        this.assocId = assocId;
    }

    public void setAssocType(long assocType) {
        this.assocType = assocType;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    public long getId() {
        return id;
    }

    public long getAssocId() {
        return assocId;
    }

    public long getAssocType() {
        return assocType;
    }

    public long getTypeId() {
        return typeId;
    }

    public Lang getLang() {
        return lang;
    }
}

package com.iam.oneom.core.entities.model;


import com.google.gson.annotations.SerializedName;
import com.iam.oneom.core.entities.DbEntity;
import com.iam.oneom.core.entities.HasUrl;
import com.iam.oneom.core.entities.Tagged;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Subtitle extends RealmObject implements Tagged, HasUrl, DbEntity {

    @PrimaryKey
    @SerializedName("id")
    private long id;
    @SerializedName("title")
    private String title;
    @SerializedName("url")
    private String url;
    @SerializedName("lang")
    private Lang lang;
    @SerializedName("source")
    private Source source;
    @SerializedName("file_id")
    private int fileId;
    @SerializedName("assoc_id")
    private int assocId;
    @SerializedName("assoc_type")
    private String assocType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Lang getLang() {
        return lang;
    }

    @Override
    public Quality getQuality() {
        return null;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int file_id) {
        this.fileId = file_id;
    }

    public int getAssocId() {
        return assocId;
    }

    public void setAssocId(int assocId) {
        this.assocId = assocId;
    }

    public String getAssocType() {
        return assocType;
    }

    public void setAssocType(String assocType) {
        this.assocType = assocType;
    }
}

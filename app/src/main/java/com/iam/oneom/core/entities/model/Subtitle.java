package com.iam.oneom.core.entities.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Subtitle extends RealmObject {

    @PrimaryKey
    private long id;
    private String title;
    private String url;
    private Lang lang;
    private Source source;
    private int fileId;
    private int assocId;
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

package com.iam.oneom.core.entities.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Torrent extends RealmObject {

    @PrimaryKey
    private long id;
    private Lang lang;
    private Quality quality;
    private Source source;
    private String url;
    private String title;
    private String vkPostId;
    private String value;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Lang getLang() {
        return lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    public Quality getQuality() {
        return quality;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVkPostId() {
        return vkPostId;
    }

    public void setVkPostId(String vkPostId) {
        this.vkPostId = vkPostId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

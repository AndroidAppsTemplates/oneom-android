package com.iam.oneom.core.entities.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Online extends RealmObject {

    @PrimaryKey
    private long id;
    private Source source;
    private Lang lang;
    private Quality quality;
    private String title;
    private String embedCode;
    private String url;
    private String videoUrl;
    private String posterUrl;

    public long getId() {
        return id;
    }

    public Source getSource() {
        return source;
    }

    public Lang getLang() {
        return lang;
    }

    public Quality getQuality() {
        return quality;
    }

    public String getTitle() {
        return title;
    }

    public String getEmbedCode() {
        return embedCode;
    }

    public String getUrl() {
        return url;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setEmbedCode(String embedCode) {
        this.embedCode = embedCode;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}

package com.iam.oneom.core.entities.model;

import com.iam.oneom.core.entities.HasUrl;
import com.iam.oneom.core.entities.Tagged;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.internal.KeepMember;

public class Torrent extends RealmObject implements Tagged, HasUrl {

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

    @Override
    public Lang getLang() {
        return lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    @Override
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

    @Override
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

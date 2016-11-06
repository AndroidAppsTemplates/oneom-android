package com.iam.oneom.core.entities.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Torrent extends RealmObject {

    @PrimaryKey
    private int id;
    private Lang lang;
    private Quality quality;
    private Source source;
    private String url;
    private String title;
    private String vkPostId;
    private String value;

}

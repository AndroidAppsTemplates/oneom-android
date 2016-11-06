package com.iam.oneom.core.entities.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Online extends RealmObject {

    @PrimaryKey
    private int id;
    private Source source;
    private Lang lang;
    private Quality quality;
    private String title;
    private String embedCode;
    private String url;
    private String videoUrl;
    private String posterUrl;

}

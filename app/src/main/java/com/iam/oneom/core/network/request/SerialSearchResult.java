package com.iam.oneom.core.network.request;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by iam on 11.12.16.
 */

public class SerialSearchResult extends RealmObject {

    @PrimaryKey
    private long id;
    private String title;

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
}

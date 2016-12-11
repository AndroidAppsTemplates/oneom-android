package com.iam.oneom.core.network.request;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class SerialsSearchRequest extends RealmObject {

    private String text;
    private RealmList<SerialSearchResult> results;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public RealmList<SerialSearchResult> getResults() {
        return results;
    }

    public void setResults(RealmList<SerialSearchResult> results) {
        this.results = results;
    }
}

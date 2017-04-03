package com.iam.oneom.core.network.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;

public class SerialsSearchRequest {

    @SerializedName("text")
    private String text;
    @SerializedName("serials")
    private List<SerialSearchResult> results;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<SerialSearchResult> getResults() {
        return results;
    }

    public void setResults(RealmList<SerialSearchResult> results) {
        this.results = results;
    }
}

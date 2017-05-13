package com.iam.oneom.core.network.response;

import com.google.gson.annotations.SerializedName;
import com.iam.oneom.core.entities.model.Serial;

import java.util.List;

public class SerialsSearchResponse {

    @SerializedName("text")
    private String text;
    @SerializedName("serials")
    private List<Serial> results;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Serial> getResults() {
        return results;
    }
}

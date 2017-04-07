package com.iam.oneom.core.network.response;

import com.google.gson.annotations.SerializedName;
import com.iam.oneom.core.entities.model.Episode;

import java.util.List;


public class EpsDateResponse {

    @SerializedName("count")
    private int count;
    @SerializedName("eps")
    private List<Episode> eps;

    public int getCount() {
        return count;
    }

    public EpsDateResponse setCount(int count) {
        this.count = count;
        return this;
    }

    public List<Episode> getEps() {
        return eps;
    }

    public void setEps(List<Episode> eps) {
        this.eps = eps;
    }
}

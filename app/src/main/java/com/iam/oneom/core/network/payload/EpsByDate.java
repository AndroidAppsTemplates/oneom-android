package com.iam.oneom.core.network.payload;

import com.google.gson.annotations.SerializedName;
import com.iam.oneom.core.entities.model.Episode;

import java.util.List;


public class EpsByDate {

    @SerializedName("count")
    private int count;
    @SerializedName("eps")
    private List<Episode> eps;

    public int getCount() {
        return count;
    }

    public EpsByDate setCount(int count) {
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

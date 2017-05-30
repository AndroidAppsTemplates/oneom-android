package com.iam.oneom.core.network.payload;

import com.google.gson.annotations.SerializedName;
import com.iam.oneom.core.entities.model.Episode;

import java.util.List;


public class Eps {

    @SerializedName("future_eps")
    private List<Episode> future_eps;
    @SerializedName("eps")
    private List<Episode> eps;

    public List<Episode> getFutureEps() {
        return future_eps;
    }

    public Eps setFutureEps(List<Episode> future_eps) {
        this.future_eps = future_eps;
        return this;
    }

    public List<Episode> getEps() {
        return eps;
    }

    public void setEps(List<Episode> eps) {
        this.eps = eps;
    }
}

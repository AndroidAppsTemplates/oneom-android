package com.iam.oneom.core.network.response;

import com.google.gson.annotations.SerializedName;
import com.iam.oneom.core.entities.model.Episode;

import io.realm.RealmList;


public class EpsResponse {

    @SerializedName("eps")
    private RealmList<Episode> eps;

    public RealmList<Episode> getEps() {
        return eps;
    }

    public void setEps(RealmList<Episode> eps) {
        this.eps = eps;
    }
}

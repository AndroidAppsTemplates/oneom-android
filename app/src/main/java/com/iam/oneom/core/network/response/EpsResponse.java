package com.iam.oneom.core.network.response;

import com.iam.oneom.core.entities.model.Episode;

import io.realm.RealmList;
import io.realm.RealmObject;


public class EpsResponse extends RealmObject {

    private RealmList<Episode> eps;

    public RealmList<Episode> getEps() {
        return eps;
    }

    public void setEps(RealmList<Episode> eps) {
        this.eps = eps;
    }
}

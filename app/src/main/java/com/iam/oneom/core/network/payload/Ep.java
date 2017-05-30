package com.iam.oneom.core.network.payload;

import com.google.gson.annotations.SerializedName;
import com.iam.oneom.core.entities.model.Episode;

/**
 * Created by iam on 05.03.17.
 */

public class Ep {

    @SerializedName("ep")
    private Episode episode;

    public Episode getEpisode() {
        return episode;
    }

    public void setEpisode(Episode episode) {
        this.episode = episode;
    }
}

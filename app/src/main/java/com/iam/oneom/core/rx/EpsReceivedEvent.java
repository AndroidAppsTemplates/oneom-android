package com.iam.oneom.core.rx;

import com.iam.oneom.core.entities.model.Episode;

import java.util.List;

/**
 * Created by iam on 05.03.17.
 */

public class EpsReceivedEvent {

    private final List<Episode> episodes;
    private long updatedAt;

    public EpsReceivedEvent(List<Episode> episodes, long updatedAt) {
        this.episodes = episodes;
        this.updatedAt = updatedAt;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }
}

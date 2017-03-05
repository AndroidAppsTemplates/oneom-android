package com.iam.oneom.core.rx;

import com.iam.oneom.core.entities.model.Episode;

/**
 * Created by iam on 05.03.17.
 */

public class EpisodeDateReceivedEvent {

    private final Episode episode;

    public EpisodeDateReceivedEvent(Episode episode) {
        this.episode = episode;
    }

    public Episode getEpisode() {
        return episode;
    }
}

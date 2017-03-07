package com.iam.oneom.core.rx;

/**
 * Created by iam on 05.03.17.
 */

public class EpisodeImageTintDefinedEvent {

    private final int colorTint;

    public EpisodeImageTintDefinedEvent(int colorTint) {
        this.colorTint = colorTint;
    }

    public int getTint() {
        return colorTint;
    }
}

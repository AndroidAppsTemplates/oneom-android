package com.iam.oneom.core.rx;

/**
 * Created by iam on 07.04.17.
 */

public class UpdateFinishedEvent {
    private boolean isFinished;
    private Throwable throwable;

    public UpdateFinishedEvent(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public UpdateFinishedEvent(boolean isFinished, Throwable throwable) {
        this(isFinished);
        this.throwable = throwable;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}

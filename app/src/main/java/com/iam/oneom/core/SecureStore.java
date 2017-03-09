package com.iam.oneom.core;

import com.orhanobut.hawk.Hawk;

/**
 * Created by iam on 08.03.17.
 */

public final class SecureStore {
    private SecureStore() {
    }

    private static final String EPISODES_SCHELDULE_LAST_UPDATED = "EPISODES_SCHEDULE_LAST_UPDATED";


    public static final void setEpisodesLastUpdated(long lastUpdated) {
        Hawk.put(EPISODES_SCHELDULE_LAST_UPDATED, lastUpdated);
    }

    public static final long getEpisodesLastUpdated() {
        return Hawk.get(EPISODES_SCHELDULE_LAST_UPDATED, 0L);
    }

}

package com.iam.oneom.core.update;

import com.iam.oneom.core.update.phase.EpisodesPhase;
import com.iam.oneom.core.update.phase.Phase;
import com.iam.oneom.core.update.phase.SettingsPhase;

/**
 * Created by iam on 07.04.17.
 */

public enum UpdatePhase {
    SETTINGS(new SettingsPhase()),
    SERIALS(new EpisodesPhase());

    UpdatePhase(Phase phase) {
        this.phase = phase;
    }

    private Phase phase;

    public Phase body() {
        return phase;
    }

    public static String[] names(UpdatePhase[] phases) {
        if (phases == null || phases.length == 0) {
            return new String[0];
        }

        String[] names = new String[phases.length];
        for (int i = 0; i < phases.length; i++) {
            names[i] = phases[i].name();
        }

        return names;
    }

    public static UpdatePhase[] phases(String[] names) {
        if (names == null || names.length == 0) {
            return new UpdatePhase[0];
        }

        UpdatePhase[] phases = new UpdatePhase[names.length];
        for (int i = 0; i < names.length; i++) {
            phases[i] = UpdatePhase.valueOf(names[i]);
        }

        return phases;
    }
}

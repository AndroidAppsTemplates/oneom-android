package com.iam.oneom.core.update.phase;

import com.iam.oneom.core.update.UpdateException;

/**
 * Created by iam on 07.04.17.
 */

public abstract class Phase {

    public abstract void run() throws UpdateException;

    public abstract long lastUpdated();
}

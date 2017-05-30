package com.iam.oneom.core.network.update.phase;

import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.network.Web;
import com.iam.oneom.core.network.payload.Eps;
import com.iam.oneom.core.network.payload.EpsByDate;
import com.iam.oneom.core.network.update.UpdateException;
import com.iam.oneom.util.Time;

/**
 * Created by iam on 07.04.17.
 */

public class EpisodesPhase extends Phase {
    @Override
    public void run() throws UpdateException {
        if (System.currentTimeMillis() - lastUpdated() < Time.MONTH) {
            EpsByDate response = Web.instance.getDateEpisodesFlat(Time.toString(lastUpdated()));

            for (Episode episode : response.getEps()) {
                episode.setIsSheldule(true);
            }
            DbHelper.insertAll(response.getEps());
        } else {
            Eps response = Web.instance.getLastEpisodesFlat();

            for (Episode episode : response.getEps()) {
                episode.setIsSheldule(true);
            }
            DbHelper.insertAll(response.getEps());

            for (Episode episode : response.getFutureEps()) {
                episode.setIsSheldule(true);
            }
            DbHelper.insertAll(response.getFutureEps());
        }

    }

    @Override
    public long lastUpdated() {
        Number updatedAt = DbHelper.where(Episode.class).max("updatedAt");
        return updatedAt == null ? 0L : updatedAt.longValue();
    }
}

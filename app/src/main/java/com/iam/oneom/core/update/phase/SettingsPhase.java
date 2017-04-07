package com.iam.oneom.core.update.phase;

import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.network.Web;
import com.iam.oneom.core.network.response.DataConfigResponse;
import com.iam.oneom.core.update.UpdateException;

/**
 * Created by iam on 07.04.17.
 */

public class SettingsPhase extends Phase {

    @Override
    public void run() throws UpdateException {
        DataConfigResponse response = Web.instance.getInitialDataFlat();
        if (response == null) {
            return;
        }

        DbHelper.insertAll(response.getCountries());
        DbHelper.insertAll(response.getGenres());
        DbHelper.insertAll(response.getLang());
        DbHelper.insertAll(response.getNetworks());
        DbHelper.insertAll(response.getQualities());
        DbHelper.insertAll(response.getQualityGroups());
        DbHelper.insertAll(response.getSources());
        DbHelper.insertAll(response.getStatuses());
    }

    @Override
    public long lastUpdated() {
        return 0L;
    }
}

package com.iam.oneom.core.util;

import com.iam.oneom.core.entities.DbUtil;
import com.iam.oneom.core.entities.model.Episode;

import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList;

/**
 * Created by iam on 19.05.17.
 */

public class DiffObservableDbCallback implements DiffObservableList.Callback<Episode> {

    @Override
    public boolean areItemsTheSame(Episode oldItem, Episode newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(Episode oldItem, Episode newItem) {
        return DbUtil.episodeInSeasonString(oldItem).equals(DbUtil.episodeInSeasonString(newItem));
    }
}

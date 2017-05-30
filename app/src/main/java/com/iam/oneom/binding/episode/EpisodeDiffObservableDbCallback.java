package com.iam.oneom.binding.episode;

import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.util.OneOmUtil;

import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList;

/**
 * Created by iam on 19.05.17.
 */

public class EpisodeDiffObservableDbCallback implements DiffObservableList.Callback<Episode> {

    @Override
    public boolean areItemsTheSame(Episode oldItem, Episode newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(Episode oldItem, Episode newItem) {
        return OneOmUtil.episodeInSeasonString(oldItem).equals(OneOmUtil.episodeInSeasonString(newItem));
    }
}

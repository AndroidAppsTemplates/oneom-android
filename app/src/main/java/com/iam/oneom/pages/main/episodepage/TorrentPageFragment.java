package com.iam.oneom.pages.main.episodepage;

import android.os.Bundle;

import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.Tagged;
import com.iam.oneom.core.entities.Util;
import com.iam.oneom.core.entities.model.Source;

import java.util.List;

public class TorrentPageFragment extends BaseSearchListFragment {

    private static final String ID_EXTRA = "ID_EXTRA";

    public static TorrentPageFragment getFragment(long id) {
        TorrentPageFragment fragment = new TorrentPageFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ID_EXTRA, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void startNextActivity(Source source) {

    }

    @Override
    protected List<? extends Tagged> getRelatedItems() {
        return getEpisode().getTorrent();
    }

    @Override
    protected List<Source> getSources() {
        return DbHelper.where(Source.class).equalTo("typeId", Source.TORRENT).findAll();
    }

    @Override
    protected <T extends Tagged> String getRelatedText(T tagged) {
        return Util.qualityTag(tagged);
    }


}

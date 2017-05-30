package com.iam.oneom.pages.main.episode.sources;

import android.os.Bundle;

import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.model.Source;
import com.iam.oneom.pages.main.search.BaseSearchActivity;
import com.iam.oneom.util.OneOmUtil;

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
        BaseSearchActivity.start(getActivity(), OneOmUtil.searchString(getEpisode()), source.getId(), getEpisode().getId());
    }

    @Override
    protected List<Source> getSources() {
        return DbHelper.where(Source.class).equalTo("typeId", Source.TORRENT).findAll();
    }
}

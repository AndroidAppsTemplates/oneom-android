package com.iam.oneom.pages.main.episode;

import android.os.Bundle;

import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.DbUtil;
import com.iam.oneom.core.entities.model.Source;
import com.iam.oneom.pages.main.search.BaseSearchActivity;

import java.util.List;

public class SubtitlesPageFragment extends BaseSearchListFragment {

    private static final String ID_EXTRA = "ID_EXTRA";

    public static SubtitlesPageFragment getFragment(long id) {
        SubtitlesPageFragment fragment = new SubtitlesPageFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ID_EXTRA, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void startNextActivity(Source source) {
        BaseSearchActivity.start(getActivity(), DbUtil.searchString(getEpisode()), source.getId(), getEpisode().getId());
    }

    @Override
    protected List<Source> getSources() {
        return DbHelper.where(Source.class).equalTo("typeId", Source.SUBTITLE).findAll();
    }
}

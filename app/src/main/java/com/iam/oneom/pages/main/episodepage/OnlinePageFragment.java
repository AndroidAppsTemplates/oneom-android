package com.iam.oneom.pages.main.episodepage;

import android.os.Bundle;

import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.Tagged;
import com.iam.oneom.core.entities.Util;
import com.iam.oneom.core.entities.model.Source;
import com.iam.oneom.pages.main.search.OnlinesSearchActivity;

import java.util.List;

public class OnlinePageFragment extends BaseSearchListFragment {

    private static final String ID_EXTRA = "ID_EXTRA";

    public static OnlinePageFragment getFragment(long id) {
        OnlinePageFragment fragment = new OnlinePageFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ID_EXTRA, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void startNextActivity(Source source) {
        OnlinesSearchActivity.start(getActivity(), OnlinesSearchActivity.class, source.getId(), getEpisode().getId());
    }

    @Override
    protected List<? extends Tagged> getRelatedItems() {
        return getEpisode().getOnline();
    }

    @Override
    protected List<Source> getSources() {
        return DbHelper.where(Source.class).equalTo("typeId", Source.ONLINE).findAll();
    }

    @Override
    protected <T extends Tagged> String getRelatedText(T tagged) {
        return Util.qualityTag(tagged);
    }


}

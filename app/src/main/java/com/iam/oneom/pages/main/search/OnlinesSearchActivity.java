package com.iam.oneom.pages.main.search;

import java.util.List;

/**
 * Created by iam on 03.04.17.
 */

public class OnlinesSearchActivity extends BaseSearchActivity<OnlineSearchResult> {


    @Override
    public void onSearchFinished(List<OnlineSearchResult> results) {

    }

    @Override
    public void performAction(OnlineSearchResult searchResult) {

    }

    @Override
    protected Presenter<OnlineSearchResult> getPresenter() {
        return new OnlinesPresenter(this, getSource());
    }
}

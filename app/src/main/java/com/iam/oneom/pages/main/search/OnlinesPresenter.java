package com.iam.oneom.pages.main.search;

import com.iam.oneom.core.entities.model.Source;

import java.util.List;

/**
 * Created by iam on 06.04.17.
 */

public class OnlinesPresenter implements Presenter<OnlineSearchResult>, SearchDataSource.OnSearchListener<OnlineSearchResult> {

    private DisplayView<OnlineSearchResult> view;
    private SearchDataSource<OnlineSearchResult> searchDataSource;
    private Source source;

    public OnlinesPresenter(DisplayView<OnlineSearchResult> view, Source source) {
        this.view = view;
        this.source = source;
        this.searchDataSource = new OnlinesSearchDataSource(this);
    }

    @Override
    public void onResume(String searchString) {
        searchDataSource.search(searchString, source.getSearchPage());
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void getResults(List<OnlineSearchResult> results) {

    }
}

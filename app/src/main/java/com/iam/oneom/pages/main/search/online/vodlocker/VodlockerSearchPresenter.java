package com.iam.oneom.pages.main.search.online.vodlocker;

import com.iam.oneom.core.entities.model.Source;
import com.iam.oneom.pages.main.search.DisplayView;
import com.iam.oneom.pages.main.search.Presenter;
import com.iam.oneom.pages.main.search.SearchDataSource;

import java.util.List;

/**
 * Created by iam on 06.04.17.
 */

public class VodlockerSearchPresenter implements Presenter<VodlockerSearchResult>, SearchDataSource.OnSearchListener<VodlockerSearchResult> {

    private DisplayView<VodlockerSearchResult> view;
    private SearchDataSource<VodlockerSearchResult> searchDataSource;
    private Source source;

    public VodlockerSearchPresenter(DisplayView<VodlockerSearchResult> view, Source source) {
        this.view = view;
        this.source = source;
        this.searchDataSource = new VodlockerDataSource(this);
    }

    @Override
    public void onResume(String searchString) {
        searchDataSource.search(searchString, source.getSearchPage());
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void getResults(List<VodlockerSearchResult> results) {
        view.onSearchFinished(results);
    }
}

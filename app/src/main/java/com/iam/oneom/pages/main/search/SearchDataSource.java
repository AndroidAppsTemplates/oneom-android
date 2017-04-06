package com.iam.oneom.pages.main.search;

import java.util.List;

/**
 * Created by iam on 06.04.17.
 */

public abstract class SearchDataSource<T> {

    interface OnSearchListener<T> {
        void getResults(List<T> results);
    }

    OnSearchListener listener;

    public SearchDataSource(OnSearchListener<T> listener) {
        this.listener = listener;
    }

    public abstract void search(String searchString, String searchForm);

    public abstract void searchAtPage(int page);
}

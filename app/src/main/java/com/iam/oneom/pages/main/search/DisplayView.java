package com.iam.oneom.pages.main.search;

import java.util.List;

/**
 * Created by iam on 06.04.17.
 */

public interface DisplayView<T> {

    void onSearchFinished(List<T> results);
    void showProgress();
    void hideProgress();
    void performAction(T searchResult);

}

package com.iam.oneom.pages.main.search;

/**
 * Created by iam on 06.04.17.
 */

public interface Presenter<T> {

    void onResume(String searchString);
    void onDestroy();

}

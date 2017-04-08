package com.iam.oneom.pages.mpd;

import java.util.List;

/**
 * Created by iam on 08.04.17.
 */

public interface View<T> {

    void showProgress();
    void hideProgress();
    void show(List<T> list);
    void onItemClick(T item);
    void reportError(Throwable t);
}

package com.iam.oneom.pages.mpd;

/**
 * Created by iam on 08.04.17.
 */

public interface PagingPresenter<T> extends Presenter<T>  {

    void loadMore();
}

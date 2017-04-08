package com.iam.oneom.pages.mpd;

/**
 * Created by iam on 08.04.17.
 */

public interface PagingDataSource<T> extends DataSource<T> {

    void getPrevious();

}

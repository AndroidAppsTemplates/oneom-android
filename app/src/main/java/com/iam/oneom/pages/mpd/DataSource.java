package com.iam.oneom.pages.mpd;

import android.content.Context;

import java.util.List;

/**
 * Created by iam on 08.04.17.
 */

public interface DataSource<T> {

    interface OnLoadListener<T> {
        void onLoad(List<T> data);
        void onError(Throwable t);
    }

    void getModels();
    void unsubscribe();
    void refresh(Context context);
}

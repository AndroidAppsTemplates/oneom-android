package com.iam.oneom.pages.mpd;

import android.content.Context;

/**
 * Created by iam on 08.04.17.
 */

public interface Presenter<T> {

    void refresh(Context context);
    void onResume();
    void onDestroy();

}

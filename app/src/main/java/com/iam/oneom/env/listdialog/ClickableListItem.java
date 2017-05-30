package com.iam.oneom.env.listdialog;

import android.view.View;

/**
 * Created by iam on 29.05.17.
 */

public interface ClickableListItem {
    View.OnClickListener getOnClick();
    String getName();
}

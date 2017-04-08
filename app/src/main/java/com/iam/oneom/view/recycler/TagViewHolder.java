package com.iam.oneom.view.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by iam on 08.04.17.
 */

class TagViewHolder extends RecyclerView.ViewHolder {

    private TextView tv;

    TagViewHolder(View itemView) {
        super(itemView);
        tv = (TextView) itemView;
    }

    void onBind(String text) {
        tv.setText(text);
    }
}
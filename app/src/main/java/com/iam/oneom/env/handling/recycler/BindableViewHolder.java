package com.iam.oneom.env.handling.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;

abstract public class BindableViewHolder extends RecyclerView.ViewHolder {

    public BindableViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void onBind(int position);
}

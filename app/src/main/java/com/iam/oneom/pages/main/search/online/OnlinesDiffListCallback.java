package com.iam.oneom.pages.main.search.online;

import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList;

/**
 * Created by iam on 27.05.17.
 */

public class OnlinesDiffListCallback<T extends OnlineSearchResult> implements DiffObservableList.Callback<T> {
    @Override
    public boolean areItemsTheSame(T oldItem, T newItem) {
        return oldItem.getDownloadUrl().equals(newItem.getDownloadUrl());
    }

    @Override
    public boolean areContentsTheSame(T oldItem, T newItem) {
        return oldItem.getPosterUrl().equals(newItem.getDownloadUrl());
    }
}

package com.iam.oneom.pages.main.search.online;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;

import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by iam on 16.05.17.
 */

public abstract class OnlinesSearchViewModel<T extends OnlineSearchResult> {

    private ObservableList<T> items = new ObservableArrayList<>();
    private ItemBinding<T> binding = getBinding();
    private ObservableBoolean loading = new ObservableBoolean();

    protected abstract ItemBinding<T> getBinding();

    public void setLoading(boolean loading) {
        this.loading.set(loading);
    }

    public void addAll(List<T> list) {
        items.addAll(list);
    }

    public void add(T item) {
        items.add(item);
    }

    public void set(List<T> list) {
        items.clear();
        items.addAll(list);
    }

    public void set(T item) {
        items.clear();
        items.add(item);
    }
}

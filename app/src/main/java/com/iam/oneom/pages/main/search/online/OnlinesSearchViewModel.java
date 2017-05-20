package com.iam.oneom.pages.main.search.online;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import com.iam.oneom.BR;
import com.iam.oneom.R;

import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by iam on 16.05.17.
 */

public abstract class OnlinesSearchViewModel<T extends OnlineSearchResult> {

    public ObservableList<T> items = new ObservableArrayList<>();
    public ItemBinding<T> binding = ItemBinding.of(BR.result, R.layout.online_grid_item);
    public ObservableBoolean loading = new ObservableBoolean();
    public ObservableField<String> searchString = new ObservableField<>();

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

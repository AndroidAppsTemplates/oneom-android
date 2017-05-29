package com.iam.oneom.view.listdialog;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Parcelable;

import com.iam.oneom.R;

import java.util.List;

import me.tatarka.bindingcollectionadapter2.BR;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by iam on 29.05.17.
 */

public class ListDialogViewModel<T extends Parcelable & ClickableListItem> {
    public ObservableList<T> list;
    public ItemBinding<T> binding = ItemBinding.of(BR.item, R.layout.clickable_item);

    public ListDialogViewModel(List<T> list) {
        this.list = new ObservableArrayList<>();
        this.list.addAll(list);
    }
}

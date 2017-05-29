package com.iam.oneom.binding;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.support.v7.widget.RecyclerView;

import me.tatarka.bindingcollectionadapter2.BindingCollectionAdapter;

/**
 * Created by iam on 28.05.17.
 */

public class RecyclerViewBinding {

    private RecyclerViewBinding() {
    }

    @BindingAdapter("onItemClick")
    public static void bindOnItemClick(RecyclerView recycler, ItemClickSupport.OnItemClickListener listener) {
        ItemClickSupport.addTo(recycler).setOnlyClickable(true).setOnItemClickListener(listener);
    }

    @SuppressWarnings("unchecked")
    @BindingConversion
    public static <T> ItemClickSupport.OnItemClickListener itemClick(ItemClickSupport.OnItemClick<T> listener) {
        return (recyclerView, itemView, position) -> {
            T adapterItem = (T) ((BindingCollectionAdapter) recyclerView.getAdapter()).getAdapterItem(position);
            listener.onItemClicked(recyclerView, itemView, position, adapterItem);
        };
    }

    @BindingAdapter("onItemLongClick")
    public static void bindOnItemLongClick(RecyclerView recycler, ItemClickSupport.OnItemLongClickListener listener) {
        ItemClickSupport.addTo(recycler).setOnlyClickable(true).setOnItemLongClickListener(listener);
    }

    @SuppressWarnings("unchecked")
    @BindingConversion
    public static <T> ItemClickSupport.OnItemLongClickListener itemClick(ItemClickSupport.OnItemLongClick<T> listener) {
        return (recyclerView, itemView, position) -> {
            T adapterItem = (T) ((BindingCollectionAdapter) recyclerView.getAdapter()).getAdapterItem(position);
            return listener.onItemLongClicked(recyclerView, itemView, position, adapterItem);
        };
    }
}
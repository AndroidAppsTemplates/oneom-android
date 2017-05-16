package com.iam.oneom.pages.main.episodes;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.graphics.Bitmap;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.iam.oneom.BR;
import com.iam.oneom.R;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.util.Time;
import com.iam.oneom.pages.mpd.PagingPresenter;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by iam on 09.05.17.
 */

public class ViewModel {

    public final ObservableList<Episode> items = new ObservableArrayList<>();
    public final ObservableBoolean loading = new ObservableBoolean(false);
    public final ObservableField<String> lastUpdated;
    public final ItemBinding<Episode> itemBinding = ItemBinding.of(BR.ep, R.layout.episodes_list_item_bind);

    public ViewModel(Context context) {
        lastUpdated = new ObservableField<>(Time.episodesLastUpdated(context));
    }


    @BindingAdapter({"presenter", "viewModel"})
    public static void setPaddingLeft(SwipyRefreshLayout view, PagingPresenter presenter, ViewModel viewModel) {
        view.setOnRefreshListener(direction -> {
            viewModel.loading.set(true);
            if (direction == SwipyRefreshLayoutDirection.TOP) {
                presenter.refresh(view.getContext());
            } else {
                presenter.loadMore();
            }
        });
    }

    @BindingAdapter("image")
    public static void setBackground(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @BindingAdapter("onMenuItemClick")
    public static void setOnMenuItemClickListener(Toolbar toolbar, Toolbar.OnMenuItemClickListener listener) {
        toolbar.setOnMenuItemClickListener(listener);
    }
}
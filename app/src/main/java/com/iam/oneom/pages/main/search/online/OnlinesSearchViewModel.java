package com.iam.oneom.pages.main.search.online;

import android.databinding.ObservableField;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.iam.oneom.BR;
import com.iam.oneom.binding.ItemClickSupport;
import com.iam.oneom.core.CustomRequest;
import com.iam.oneom.core.entities.model.Source;
import com.iam.oneom.core.rx.RxUtils;

import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList;
import rx.Subscription;

/**
 * Created by iam on 16.05.17.
 */

public abstract class OnlinesSearchViewModel<T extends OnlineSearchResult> {

    public DiffObservableList<T> items = new DiffObservableList<>(new OnlinesDiffListCallback<>());
    public ItemBinding<T> binding = ItemBinding.of(BR.item, com.iam.oneom.R.layout.online_grid_item);
    public ObservableField<Boolean> loading = new ObservableField<>(false);
    public ObservableField<String> searchString = new ObservableField<>();
    public ObservableField<Boolean> justLoaded = new ObservableField<>(true);

    public Source source;

    private String url;
    private OnlineParser<T> parser;

    public OnlinesSearchViewModel(OnlineParser<T> parser, Source source, String searchString) {
        this.url = source.getSearch();
        this.parser = parser;
        this.searchString.set(searchString);
        binding = getBinding();
    }

    protected abstract ItemBinding<T> getBinding();

    private Subscription subscription;

    public void setLoading(boolean loading) {
        this.loading.set(loading);
    }

    public void onResume() {
        if (subscription != null && !subscription.isUnsubscribed()) return;
        search(searchString.get());
    }

    private void search(String searchString) {
        String url = this.url
                .replace("{searchString}", searchString)
                .replace("{page}", "1");
        loading.set(true);
        subscription =
                CustomRequest.instance.request(url)
                .subscribe(s -> {
                    justLoaded.set(false);
                    loading.set(false);
                    items.update(parser.parse(s));
                });
    }

    public TextWatcher onTextEdit = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            RxUtils.unsubscribe(subscription);
            search(s.toString());
        }
    };

    public ItemClickSupport.OnItemClickListener onItemClick = new ItemClickSupport.OnItemClickListener(){
        @Override
        public void onItemClicked(RecyclerView recyclerView, View itemView, int position) {
            items.get(position).loadVideo(loading);
        }
    };

    public void onDestroy() {
        RxUtils.unsubscribe(subscription);
    }
}

package com.iam.oneom.pages.main.search.online;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.iam.oneom.R;
import com.iam.oneom.pages.main.search.BaseSearchActivity;

/**
 * Created by iam on 16.05.17.
 */

public abstract class OnlinesSearchActivity<T extends OnlineSearchResult> extends BaseSearchActivity<T> {

    private OnlinesSearchViewModel<T> viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = createViewModel();
    }

    @Override
    protected int getLayout() {
        return R.layout.onlines_search_activity;
    }

    protected abstract OnlinesSearchViewModel<T> createViewModel();

    public OnlinesSearchViewModel<T> getViewModel() {
        return viewModel;
    }
}

package com.iam.oneom.pages.main.search.online;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.iam.oneom.databinding.OnlinesSearchActivityBinding;
import com.iam.oneom.pages.main.search.BaseSearchActivity;

/**
 * Created by iam on 16.05.17.
 */

public abstract class OnlinesSearchActivity<T extends OnlineSearchResult> extends BaseSearchActivity {

    private OnlinesSearchViewModel<T> viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnlinesSearchActivityBinding binding = DataBindingUtil.setContentView(this, getLayout());
        viewModel = createViewModel();
        binding.setVm(viewModel);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
    }

    @Override
    protected int getLayout() {
        return com.iam.oneom.R.layout.onlines_search_activity;
    }

    protected abstract OnlinesSearchViewModel<T> createViewModel();
}

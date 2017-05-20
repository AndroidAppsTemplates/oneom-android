package com.iam.oneom.pages.main.search.online.vodlocker;

import com.iam.oneom.pages.main.search.Presenter;
import com.iam.oneom.pages.main.search.online.OnlinesSearchActivity;
import com.iam.oneom.pages.main.search.online.OnlinesSearchViewModel;

import java.util.List;

/**
 * Created by iam on 03.04.17.
 */

public class VodlockerSearchActivity extends OnlinesSearchActivity<VodlockerSearchResult> {

    @Override
    public void onSearchFinished(List<VodlockerSearchResult> results) {
        getViewModel().set(results);
    }

    @Override
    public void showProgress() {
        getViewModel().loading.set(true);
    }

    @Override
    public void hideProgress() {
        getViewModel().loading.set(false);
    }

    @Override
    public void performAction(VodlockerSearchResult searchResult) {

    }

    @Override
    protected Presenter<VodlockerSearchResult> getPresenter() {
        switch (getSource().getName().trim().toLowerCase()) {
            case "vodlocker":
                return new VodlockerSearchPresenter(this, getSource());
        }

        return null;
    }

    @Override
    protected OnlinesSearchViewModel<VodlockerSearchResult> createViewModel() {
        return new VodlockerViewModel();
    }
}

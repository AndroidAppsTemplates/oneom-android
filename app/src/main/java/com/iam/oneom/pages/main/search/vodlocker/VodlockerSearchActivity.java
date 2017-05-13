package com.iam.oneom.pages.main.search.vodlocker;

import com.iam.oneom.pages.main.search.BaseSearchActivity;
import com.iam.oneom.pages.main.search.Presenter;

import java.util.List;

/**
 * Created by iam on 03.04.17.
 */

public class VodlockerSearchActivity extends BaseSearchActivity<VodlockerSearchResult> {


    @Override
    public void onSearchFinished(List<VodlockerSearchResult> results) {

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
}

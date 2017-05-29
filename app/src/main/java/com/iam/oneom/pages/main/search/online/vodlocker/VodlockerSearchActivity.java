package com.iam.oneom.pages.main.search.online.vodlocker;

import com.iam.oneom.pages.main.search.online.OnlinesSearchActivity;
import com.iam.oneom.pages.main.search.online.OnlinesSearchViewModel;

/**
 * Created by iam on 03.04.17.
 */

public class VodlockerSearchActivity extends OnlinesSearchActivity<VodlockerSearchResult> {

    @Override
    protected OnlinesSearchViewModel<VodlockerSearchResult> createViewModel() {
        return new VodlockerSearchResultsViewModel(new VodlokerParser(), getSource(), getSearchString());
    }
}

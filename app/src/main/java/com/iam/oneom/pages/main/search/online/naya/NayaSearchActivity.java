package com.iam.oneom.pages.main.search.online.naya;

import com.iam.oneom.pages.main.search.online.OnlinesSearchActivity;
import com.iam.oneom.pages.main.search.online.OnlinesSearchViewModel;

/**
 * Created by iam on 03.04.17.
 */

public class NayaSearchActivity extends OnlinesSearchActivity<NayaSearchResult> {

    @Override
    protected OnlinesSearchViewModel<NayaSearchResult> createViewModel() {
        return new NayaSearchResultsViewModel(new NayaParser(), getSource(), getSearchString().replaceAll(" ", "."));
    }
}

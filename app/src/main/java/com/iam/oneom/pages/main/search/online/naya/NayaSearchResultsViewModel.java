package com.iam.oneom.pages.main.search.online.naya;

import com.iam.oneom.BR;
import com.iam.oneom.R;
import com.iam.oneom.core.entities.model.Source;
import com.iam.oneom.pages.main.search.online.OnlineParser;
import com.iam.oneom.pages.main.search.online.OnlinesSearchViewModel;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by iam on 14.05.17.
 */

class NayaSearchResultsViewModel extends OnlinesSearchViewModel<NayaSearchResult> {

    public NayaSearchResultsViewModel(OnlineParser<NayaSearchResult> parser, Source source, String searchString) {
        super(parser, source, searchString);
    }

    @Override
    protected ItemBinding<NayaSearchResult> getBinding() {
        return ItemBinding.of(BR.item, R.layout.online_grid_item);
    }
}

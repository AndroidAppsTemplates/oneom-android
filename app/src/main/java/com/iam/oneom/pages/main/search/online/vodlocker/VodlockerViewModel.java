package com.iam.oneom.pages.main.search.online.vodlocker;

import com.iam.oneom.BR;
import com.iam.oneom.R;
import com.iam.oneom.pages.main.search.online.OnlinesSearchViewModel;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by iam on 14.05.17.
 */

class VodlockerViewModel extends OnlinesSearchViewModel<VodlockerSearchResult> {

    @Override
    protected ItemBinding<VodlockerSearchResult> getBinding() {
        return ItemBinding.of(BR.result, R.layout.online_grid_item);
    }
}

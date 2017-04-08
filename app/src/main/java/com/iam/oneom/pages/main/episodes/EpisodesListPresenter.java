package com.iam.oneom.pages.main.episodes;

import android.content.Context;

import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.pages.mpd.DataSource;
import com.iam.oneom.pages.mpd.PagingDataSource;
import com.iam.oneom.pages.mpd.PagingPresenter;
import com.iam.oneom.pages.mpd.View;

import java.util.List;

/**
 * Created by iam on 08.04.17.
 */

class EpisodesListPresenter implements PagingPresenter<Episode>, DataSource.OnLoadListener<Episode> {

    private View<Episode> view;
    private PagingDataSource<Episode> dataSource;

    public EpisodesListPresenter(View<Episode> view) {
        this.view = view;
        dataSource = new EpisodesDataSource(this);
    }

    @Override
    public void refresh(Context context) {
        dataSource.refresh(context);
    }

    @Override
    public void onResume() {
        dataSource.getModels();
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void loadMore() {
        dataSource.getPrevious();
    }

    @Override
    public void onLoad(List<Episode> data) {
        view.show(data);
        view.hideProgress();
    }

    @Override
    public void onError(Throwable t) {
        view.reportError(t);
        view.hideProgress();
    }
}

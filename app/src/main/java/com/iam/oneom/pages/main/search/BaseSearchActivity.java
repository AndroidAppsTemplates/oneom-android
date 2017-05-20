package com.iam.oneom.pages.main.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.entities.model.Source;
import com.iam.oneom.pages.main.search.online.vodlocker.VodlockerSearchActivity;

/**
 * Created by iam on 03.04.17.
 */

public abstract class BaseSearchActivity<T> extends AppCompatActivity implements DisplayView<T> {

    private Episode episode;
    private Source source;
    private String searchString;


    private Presenter<T> presenter;

    public static final String EP_ID_EXTRA = "EP_ID_EXTRA";
    public static final String SOURCE_ID_EXTRA = "SOURCE_ID_EXTRA";
    public static final String STRING_SEARCH_EXTRA = "STRING_SEARCH_EXTRA";

    public static final void start(Context context, String searchString, long sourceId, long epId) {
        Source source = DbHelper.where(Source.class).equalTo("id", sourceId).findFirst();
        Intent intent = new Intent(context, getActivityClass(source));
        intent.putExtra(EP_ID_EXTRA, epId);
        intent.putExtra(SOURCE_ID_EXTRA, sourceId);
        intent.putExtra(STRING_SEARCH_EXTRA, searchString);
        context.startActivity(intent);
    }

    private static Class<?> getActivityClass(Source source) {
        switch (source.getName()) {
            case Source.VODLOCKER:
                return VodlockerSearchActivity.class;
        }

        return NoSourceActivity.class;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchString = getIntent().getStringExtra(STRING_SEARCH_EXTRA);

        episode = DbHelper
                .where(Episode.class)
                .equalTo("id", getIntent().getLongExtra(EP_ID_EXTRA, 0))
                .findFirst();

        source = DbHelper
                .where(Source.class)
                .equalTo("id", getIntent().getLongExtra(SOURCE_ID_EXTRA, 0))
                .findFirst();

//        editText.setText(searchString);

        presenter = getPresenter();
    }

    protected abstract int getLayout();

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume(getSearchString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    protected abstract Presenter<T> getPresenter();

    public String getSearchString() {
        return searchString;
    }

    public Episode getEpisode() {
        return episode;
    }

    public Source getSource() {
        return source;
    }
}

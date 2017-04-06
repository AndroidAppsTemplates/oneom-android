package com.iam.oneom.pages.main.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.iam.oneom.R;
import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.entities.model.Source;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by iam on 03.04.17.
 */

public abstract class BaseSearchActivity<T> extends AppCompatActivity implements DisplayView<T> {

    private Episode episode;
    private Source source;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.search_button)
    Button searchButton;
    @BindView(R.id.edit_text)
    EditText editText;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cpv)
    CircularProgressView cpv;

    private Presenter<T> presenter;

    public static final String EP_ID_EXTRA = "EP_ID_EXTRA";
    public static final String SOURCE_ID_EXTRA = "SOURCE_ID_EXTRA";

    public static final void start(Context context, Class<?> cls, long sourceId, long epId) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(EP_ID_EXTRA, epId);
        intent.putExtra(SOURCE_ID_EXTRA, sourceId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        ButterKnife.bind(this);

        episode = DbHelper
                .where(Episode.class)
                .equalTo("id", getIntent().getLongExtra(EP_ID_EXTRA, 0))
                .findFirst();

        source = DbHelper
                .where(Source.class)
                .equalTo("id", getIntent().getLongExtra(SOURCE_ID_EXTRA, 0))
                .findFirst();

        presenter = getPresenter();
    }

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
        return editText.getText().toString();
    }

    public Episode getEpisode() {
        return episode;
    }

    public Source getSource() {
        return source;
    }

    public void showProgress() {
        cpv.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        cpv.setVisibility(View.GONE);
    }
}

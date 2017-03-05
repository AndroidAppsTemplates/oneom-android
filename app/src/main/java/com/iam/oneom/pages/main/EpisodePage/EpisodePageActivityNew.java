package com.iam.oneom.pages.main.EpisodePage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.iam.oneom.R;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.network.Web;
import com.iam.oneom.core.network.response.EpResponse;
import com.iam.oneom.env.widget.EpisodePageAdapter;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EpisodePageActivityNew extends AppCompatActivity implements Callback<EpResponse> {

    private static final String TAG = EpisodePageActivityNew.class.getSimpleName();
    private static final String EP_ID_EXTRA = "EP_ID_EXTRA";

    Episode episode;

    public static void open(Context context, long epId) {
        Intent intent = new Intent(context, EpisodePageActivityNew.class);
        intent.putExtra(EP_ID_EXTRA, epId);
        context.startActivity(intent);
    }

    @BindView(R.id.poster)
    public ImageView imagePoster;
    @BindView(R.id.posterImage)
    public ImageView smallPoster;
    @BindView(R.id.description)
    public TextView description;
    @BindView(R.id.desc_title)
    public TextView descTitle;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.bluring_area)
    public FrameLayout blurArea;
    @BindView(R.id.tabs)
    public TabLayout tabLayout;

    @BindDimen(R.dimen.ep_poster_corner_radius)
    public int cornerRadius;

    EpisodePageAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.episode_page_activity);
        ButterKnife.bind(this);
        long id = getIntent().getLongExtra(EP_ID_EXTRA, 0);
        episode = Realm.getDefaultInstance().where(Episode.class).equalTo("id", id).findFirst();
        adapter = new EpisodePageAdapter(this);
        adapter.onCreate(episode);
        Web.instance.getEpisode(episode.getId()).enqueue(this);
    }

    @Override
    public void onResponse(Call<EpResponse> call, Response<EpResponse> response) {
        adapter.onEpisodeGetted(response.body().getEpisode());
    }

    @Override
    public void onFailure(Call<EpResponse> call, Throwable t) {

    }
}

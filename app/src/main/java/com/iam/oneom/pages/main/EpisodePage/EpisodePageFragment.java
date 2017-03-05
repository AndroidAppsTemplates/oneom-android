package com.iam.oneom.pages.main.EpisodePage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iam.oneom.R;
import com.iam.oneom.core.entities.model.Episode;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;


public class EpisodePageFragment extends Fragment {

    private static final String ID_EXTRA = "ID_EXTRA";

    @BindView(R.id.posterImage)
    public ImageView smallPoster;
    @BindView(R.id.description)
    public TextView description;
    @BindView(R.id.desc_title)
    public TextView descTitle;

    private View view;

    private EpisodePageFragmentAdapter adapter;
    private Episode episode;

    public static EpisodePageFragment getFragment(long id) {
        EpisodePageFragment fragment = new EpisodePageFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ID_EXTRA, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.episode_page_episode_page, container, false);
        ButterKnife.bind(this, view);
        long id = getArguments().getLong(ID_EXTRA);
        episode = Realm.getDefaultInstance().where(Episode.class).equalTo("id", id).findFirst();
        adapter = new EpisodePageFragmentAdapter(this, episode);
        adapter.onCreate();
        return view;
    }
}
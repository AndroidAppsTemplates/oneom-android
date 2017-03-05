package com.iam.oneom.pages.main.EpisodePage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iam.oneom.R;
import com.iam.oneom.core.entities.model.Episode;

import io.realm.Realm;

public class OnlinePageFragment extends Fragment {

    private static final String ID_EXTRA = "ID_EXTRA";

    private Episode episode;
    protected RecyclerView view;
    private OnlinePageFragmentAdapter adapter;

    public static OnlinePageFragment getFragment(long id) {
        OnlinePageFragment fragment = new OnlinePageFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ID_EXTRA, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (RecyclerView) inflater.inflate(R.layout.episode_page_online_page, container, false);
        long id = getArguments().getLong(ID_EXTRA);
        episode = Realm.getDefaultInstance().where(Episode.class).equalTo("id", id).findFirst();
        adapter = new OnlinePageFragmentAdapter(this, episode);
        adapter.onCreate();
        return view;
    }


}

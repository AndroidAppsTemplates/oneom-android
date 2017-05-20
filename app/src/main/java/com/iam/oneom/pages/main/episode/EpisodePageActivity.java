package com.iam.oneom.pages.main.episode;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.iam.oneom.R;
import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.databinding.EpisodePageActivityBinding;
import com.iam.oneom.pages.main.viewmodel.EpisodeViewModel;

public class EpisodePageActivity extends AppCompatActivity {

    private static final String EP_ID_EXTRA = "EP_ID_EXTRA";

    EpisodePageActivityBinding binding;
    EpisodeViewModel episodeViewModel;

    public static void open(Context context, long epId) {
        Intent intent = new Intent(context, EpisodePageActivity.class);
        intent.putExtra(EP_ID_EXTRA, epId);
        context.startActivity(intent);
    }

    public static void open(View view, long epId) {
        open(view.getContext(), epId);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.episode_page_activity);

        episodeViewModel = new EpisodeViewModel(DbHelper
                .where(Episode.class)
                .equalTo("id", getIntent().getLongExtra(EP_ID_EXTRA, 0)).findFirst());

        binding.setVm(episodeViewModel);
    }

    @Override
    protected void onResume() {
        super.onResume();
        episodeViewModel.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        episodeViewModel.onDestroy();
    }
}

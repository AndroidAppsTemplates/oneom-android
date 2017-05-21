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
import com.iam.oneom.databinding.EpisodeRelatedItemsActivityBinding;
import com.iam.oneom.pages.main.viewmodel.EpisodeViewModel;

public class EpisodeRelatedItemsActivity extends AppCompatActivity {

    private static final String EP_ID_EXTRA = "EP_ID_EXTRA";


    public static void start(Context context, long epId) {
        Intent intent = new Intent(context, EpisodeRelatedItemsActivity.class);
        intent.putExtra(EP_ID_EXTRA, epId);
        context.startActivity(intent);
    }

    public static void start(View view, long epId) {
        start(view.getContext(), epId);
    }

    private EpisodeViewModel episodeViewModel;
    private EpisodeRelatedItemsActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.episode_related_items_activity);
        episodeViewModel = new EpisodeViewModel(
                DbHelper
                        .where(Episode.class)
                        .equalTo("id", getIntent().getLongExtra(EP_ID_EXTRA, 0))
                        .findFirst()
        );
        binding.setVm(episodeViewModel);
    }
}

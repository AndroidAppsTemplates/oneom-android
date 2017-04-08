package com.iam.oneom.pages.main.episodes;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.iam.oneom.R;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.util.Time;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by iam on 08.04.17.
 */

class GroupVH extends RecyclerView.ViewHolder {

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.date)
    TextView date;

    public GroupVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }

    void onBind(Date date, List<Episode> episodes) {
        this.date.setText(Time.format(date, Time.TimeFormat.TEXT));

        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 3));
        recyclerView.setAdapter(new EpisodesGroupAdapter(episodes, recyclerView.getContext()));
    }
}
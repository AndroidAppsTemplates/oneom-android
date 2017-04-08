package com.iam.oneom.pages.main.episodes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.iam.oneom.R;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.view.recycler.EpisodeVH;

import java.util.List;

/**
 * Created by iam on 08.04.17.
 */

class EpisodesGroupAdapter extends RecyclerView.Adapter<EpisodeVH> {

    private LayoutInflater inflater;
    private List<Episode> episodes;

    public EpisodesGroupAdapter(List<Episode> episodes, Context context) {
        if (context != null) inflater = LayoutInflater.from(context);
        this.episodes = episodes;
    }

    @Override
    public EpisodeVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EpisodeVH(inflater.inflate(R.layout.episodes_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(EpisodeVH holder, int position) {
        holder.onBind(episodes.get(position));
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }
}
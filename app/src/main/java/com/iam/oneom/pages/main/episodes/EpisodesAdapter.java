package com.iam.oneom.pages.main.episodes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.iam.oneom.R;
import com.iam.oneom.core.entities.model.Episode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by iam on 08.04.17.
 */

class EpisodesAdapter extends RecyclerView.Adapter<GroupVH> {

    private Map<Date, List<Episode>> groups;
    private List<Date> keys;
    private LayoutInflater inflater;


    public EpisodesAdapter(Context context, Map<Date, List<Episode>> groups) {
        inflater = LayoutInflater.from(context);
        this.groups = groups;
        this.keys = new ArrayList(groups.keySet());
    }

    @Override
    public GroupVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GroupVH(inflater.inflate(R.layout.episodes_group_item, parent, false));
    }

    @Override
    public void onBindViewHolder(GroupVH holder, int position) {
        Date date = keys.get(position);
        holder.onBind(date, groups.get(date));
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }


    public void addGroups(Map<Date, List<Episode>> groups) {
        if (groups == null || groups.size() == 0) {
            return;
        }

        for (Map.Entry<Date, List<Episode>> entry : groups.entrySet()) {

            Date date = entry.getKey();

            if (this.groups.containsKey(date)) {
                List<Episode> alreadyContains = this.groups.get(date);
                for (Episode episode : entry.getValue()) {
                    if (!containsEpisode(alreadyContains, episode)) {
                        alreadyContains.add(episode);
                    }
                }
                notifyItemChanged(keys.indexOf(date));
            }
        }

        for (Map.Entry<Date, List<Episode>> entry : groups.entrySet()) {
            Date key = entry.getKey();
            if (!this.groups.containsKey(key)) {
                this.keys.add(key);
                this.groups.put(key, entry.getValue());
                notifyItemInserted(this.keys.indexOf(key));
            }
        }
    }

    public boolean containsEpisode(List<Episode> episodes, Episode episode) {
        long id = episode.getId();
        for (Episode e : episodes) {
            if (e.getId() == id) {
                return true;
            }
        }
        return false;
    }
}
package com.iam.oneom.pages.main.episodes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.iam.oneom.R;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.util.DateDescendingOrderComparator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class EpisodesAdapter extends RecyclerView.Adapter<GroupVH> {

    private Map<Date, List<Episode>> groups;
    private List<Date> keys;
    private LayoutInflater inflater;


    EpisodesAdapter(Context context, List<Episode> episodes) {
        inflater = LayoutInflater.from(context);
        addData(episodes);
        this.keys = new ArrayList<>(groups.keySet());
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

    void addData(List<Episode> episodes) {
        if (episodes == null || episodes.size() == 0) {
            return;
        }

        Map<Date, List<Episode>> groups = new TreeMap<>(new DateDescendingOrderComparator());

        for (Episode e : episodes) {

            Date airdate = new Date(e.getAirdate());

            if (airdate == null) {
                continue;
            }

            if (groups.get(airdate) == null) {
                groups.put(airdate, new ArrayList<>());
                groups.get(airdate).add(e);
            } else {
                groups.get(airdate).add(e);
            }
        }

        addGroups(groups);
    }

    private void addGroups(Map<Date, List<Episode>> groups) {
        if (groups == null || groups.size() == 0) {
            return;
        }

        if (this.groups == null) {
            this.groups = groups;
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

    private boolean containsEpisode(List<Episode> episodes, Episode episode) {
        long id = episode.getId();
        for (Episode e : episodes) {
            if (e.getId() == id) {
                return true;
            }
        }
        return false;
    }
}
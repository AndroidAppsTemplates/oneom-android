package com.iam.oneom.pages.main.episodes;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.iam.oneom.R;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.view.recycler.BindEpisodeVH;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

class EpisodesAdapter extends RecyclerView.Adapter<BindEpisodeVH> {

    private List<Episode> episodes = new ArrayList<>();
    private LayoutInflater inflater;


    EpisodesAdapter(Context context, List<Episode> episodes) {
        inflater = LayoutInflater.from(context);
        this.episodes = episodes;
//        addData(episodes);
    }

    @Override
    public BindEpisodeVH onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new EpisodeVH(inflater.inflate(R.layout.episodes_list_item, parent, false));
        return new BindEpisodeVH(inflater.inflate(R.layout.episodes_list_item_bind, parent, false));
    }

    @Override
    public void onBindViewHolder(BindEpisodeVH holder, int position) {
//        Date date = keys.get(position);
        holder.onBind(episodes.get(position));
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    void addData(List<Episode> episodes) {

        if (episodes == null || episodes.size() == 0) {
            return;
        }

        List<Episode> alreadyContains = new ArrayList<>();
        alreadyContains.addAll(this.episodes);
        alreadyContains.addAll(episodes);
        Collections.sort(alreadyContains, (o1, o2) -> {
            Date first = new Date(o1.getAirdate());
            Date second = new Date(o2.getAirdate());
            return first.compareTo(second);
        });

        this.episodes.clear();
        this.episodes.addAll(alreadyContains);
        notifyDataSetChanged();

//
//        Map<Date, List<Episode>> groups = new TreeMap<>(new DateDescendingOrderComparator());
//
//        for (Episode e : episodes) {
//
//            Date airdate = new Date(e.getAirdate());
//
//            if (airdate == null) {
//                continue;
//            }
//
//            if (groups.get(airdate) == null) {
//                groups.put(airdate, new ArrayList<>());
//                groups.get(airdate).add(e);
//            } else {
//                groups.get(airdate).add(e);
//            }
//        }

//        addGroups(groups);
    }

//    private void addGroups(Map<Date, List<Episode>> groups) {
//        if (groups == null || groups.size() == 0) {
//            return;
//        }
//
//        if (this.groups == null) {
//            this.groups = groups;
//            return;
//        }
//
//        for (Map.Entry<Date, List<Episode>> entry : groups.entrySet()) {
//
//            Date date = entry.getKey();
//
//            if (this.groups.containsKey(date)) {
//                List<Episode> alreadyContains = this.groups.get(date);
//                for (Episode episode : entry.getValue()) {
//                    if (!containsEpisode(alreadyContains, episode)) {
//                        alreadyContains.add(episode);
//                    }
//                }
//                notifyItemChanged(keys.indexOf(date));
//            }
//        }
//
//        for (Map.Entry<Date, List<Episode>> entry : groups.entrySet()) {
//            Date key = entry.getKey();
//            if (!this.groups.containsKey(key)) {
//                this.keys.add(key);
//                this.groups.put(key, entry.getValue());
//                notifyItemInserted(this.keys.indexOf(key));
//            }
//        }
//    }

    //    private boolean containsEpisode(List<Episode> episodes, Episode episode) {
//        long id = episode.getId();
//        for (Episode e : episodes) {
//            if (e.getId() == id) {
//                return true;
//            }
//        }
//        return false;
//    }
    public static class BindingHolder<VB extends ViewDataBinding> extends RecyclerView.ViewHolder {
        private VB mBinding;

        public static <VB extends ViewDataBinding> BindingHolder<VB> newInstance(
                @LayoutRes int layoutId, LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
            final VB vb = DataBindingUtil.inflate(inflater, layoutId, parent, attachToParent);
            return new BindingHolder<>(vb);
        }

        public BindingHolder(VB binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public VB getBinding() {
            return mBinding;
        }
    }
}
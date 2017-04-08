package com.iam.oneom.view.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.iam.oneom.R;

import java.util.List;

/**
 * Created by iam on 08.04.17.
 */

public class TagAdapter extends RecyclerView.Adapter<TagViewHolder> {

    private List<String> strings;
    private LayoutInflater inflater;

    public TagAdapter(Context context, List<String> strings) {
        inflater = LayoutInflater.from(context);
        this.strings = strings;
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TagViewHolder(inflater.inflate(R.layout.w_tag, parent, false));
    }

    @Override
    public void onBindViewHolder(TagViewHolder holder, int position) {
        holder.onBind(strings.get(position));
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }
}
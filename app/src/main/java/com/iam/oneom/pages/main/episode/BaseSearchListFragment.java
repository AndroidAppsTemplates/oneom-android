package com.iam.oneom.pages.main.episode;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iam.oneom.R;
import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.DbUtil;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.entities.model.Source;
import com.iam.oneom.env.handling.recycler.BindableViewHolder;
import com.iam.oneom.env.handling.recycler.layoutmanagers.LinearLayoutManager;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseSearchListFragment extends Fragment {

    private static final String ID_EXTRA = "ID_EXTRA";

    private Episode episode;

    private RecyclerView recyclerView;
    private Adapter adapter;

    @BindDimen(R.dimen.ep_poster_corner_radius)
    protected int cornerRadius;

    @BindColor(R.color.light)
    protected int lightColor;
    @BindColor(R.color.dark)
    protected int darkColor;
    @BindColor(R.color.middle)
    protected int middleColor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.episode_page_online_page, container, false);
        ButterKnife.bind(this, recyclerView);

        setData();

        if (episode != null) {
            configureViews();
        }

        return recyclerView;
    }

    private void setData() {
        long id = getArguments().getLong(ID_EXTRA);
        episode = DbHelper.where(Episode.class).equalTo("id", id).findFirst();
    }

    public void configureViews() {
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    protected Episode getEpisode() {
        return episode;
    }

    protected class Adapter extends RecyclerView.Adapter<BindableViewHolder> {

        List<Source> sources;

        protected Adapter() {
            sources = getSources();
        }

        @Override
        public BindableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new ItemVH(LayoutInflater.from(getActivity()).inflate(R.layout.episode_page_online_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(BindableViewHolder holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return sources.size();
        }

        protected class ItemVH extends BindableViewHolder {

            @BindView(R.id.divider)
            View divider;
            @BindView(R.id.source)
            TextView textView;
            @BindView(R.id.icon_next)
            ImageView iconNext;
            @BindColor(R.color.white)
            int active;
            @BindColor(R.color.half_gray)
            int not_active;

            private View view;

            public ItemVH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                this.view = itemView;
            }

            @Override
            public void onBind(int position) {
                Source source = sources.get(position);
                textView.setText(source.getName());
                textView.setTextColor(DbUtil.isEmptySource(source) ? not_active : active);
                divider.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
                view.setOnClickListener(v -> {
                    if (DbUtil.isEmptySource(source)) {
                        Toast.makeText(getActivity(), getString(R.string.no_searh_data, source.getName()), Toast.LENGTH_LONG).show();
                    } else {
                        startNextActivity(source);
                    }
                });
            }
        }
    }

    protected abstract void startNextActivity(Source source);

    protected abstract List<Source> getSources();
}

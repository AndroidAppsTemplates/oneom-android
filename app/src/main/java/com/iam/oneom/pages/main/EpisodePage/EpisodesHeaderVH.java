package com.iam.oneom.pages.main.EpisodePage;

import android.view.View;
import android.widget.TextView;

import com.iam.oneom.R;
import com.iam.oneom.core.entities.Util;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.entities.model.Torrent;
import com.iam.oneom.core.util.Time;
import com.iam.oneom.env.handling.recycler.BindableViewHolder;
import com.iam.oneom.pages.main.SerialPageActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

class EpisodesHeaderVH extends BindableViewHolder {

    private Episode episode;

    @BindView(R.id.serialname)
    protected TextView serialName;
    @BindView(R.id.episodename)
    protected TextView episodeName;
    @BindView(R.id.airdate)
    protected TextView airdate;

    private View view;
    private ArrayList<String> info = new ArrayList<>();

    public EpisodesHeaderVH(View itemView, Episode episode) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.episode = episode;
        this.view = itemView;
        makeInfo();
    }

    private void makeInfo() {
        if (info.size() == 0) {
            info.add(Time.format(episode.getAirdate(), Time.TimeFormat.IDN));
            info.add(episode.getTitle() + " " + Util.episodeInSeasonString(episode));
            if (episode.getTorrent() != null && episode.getTorrent().size() > 0) {
                for (Torrent torrent : episode.getTorrent()) {
                    info.add(Util.qualityTag(torrent));
                }
            }
        }
    }

    public void onBind(int position) {
        serialName.setText(episode.getSerial().getTitle());
        serialName.setOnClickListener(v -> SerialPageActivity.start(view.getContext(), episode.getSerial().getId()));
        episodeName.setText(Util.episodeInSeasonString(episode) + " " + episode.getTitle());
        airdate.setText(Time.format(episode.getAirdate(), Time.TimeFormat.TEXT));
    }
}
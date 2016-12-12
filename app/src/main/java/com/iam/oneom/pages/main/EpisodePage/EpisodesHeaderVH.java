package com.iam.oneom.pages.main.EpisodePage;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.iam.oneom.R;
import com.iam.oneom.core.entities.Util;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.entities.model.Torrent;
import com.iam.oneom.env.handling.recycler.BindableViewHolder;
import com.iam.oneom.env.widget.text.Text;
import com.iam.oneom.pages.main.SerialPageActivity;

import java.util.ArrayList;

class EpisodesHeaderVH extends BindableViewHolder {

    private Episode episode;

    private Text serialName;
    private Text episodeName;
    private Text airdate;
    private View view;
    private ArrayList<String> info = new ArrayList<>();

    public EpisodesHeaderVH(View itemView, Episode episode) {
        super(itemView);
        this.episode = episode;
        this.view = itemView;
        serialName = (Text) view.findViewById(R.id.serialname);
        episodeName = (Text) view.findViewById(R.id.episodename);
        episodeName.setVisibility(View.VISIBLE);
        airdate = (Text) view.findViewById(R.id.airdate);
        airdate.setVisibility(View.VISIBLE);
        makeInfo();
    }

    private void makeInfo() {
        if (info.size() == 0) {
            info.add(episode.getAirdate());
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
        serialName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = view.getContext();
                Intent intent = new Intent(context, SerialPageActivity.class);
                intent.putExtra(context.getString(R.string.media_page_serial_intent), episode.getSerial().getId() + "");
                context.startActivity(intent);
            }
        });
        episodeName.setText(Util.episodeInSeasonString(episode) + " " + episode.getTitle());
        airdate.setText(episode.getAirdate());
    }
}
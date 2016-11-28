package com.iam.oneom.pages.main.EpisodePage;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.iam.oneom.R;
import com.iam.oneom.core.entities.old.Episode;
import com.iam.oneom.core.entities.old.Torrent;
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
            info.add(episode.airdate());
            info.add(episode.title() + " " + episode.episodeInSeason());
            if (episode.torrent() != null && episode.torrent().size() > 0) {
                for (Torrent torrent : episode.torrent()) {
                    info.add(torrent.tagInfo());
                }
            }
        }
    }

    public void onBind(int position) {
        serialName.setText(episode.serial().title());
        serialName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = view.getContext();
                Intent intent = new Intent(context, SerialPageActivity.class);
                intent.putExtra(context.getString(R.string.media_page_serial_intent), episode.serial().id() + "");
                context.startActivity(intent);
            }
        });
        episodeName.setText(episode.episodeInSeason() + " " + episode.title());
        airdate.setText(episode.airdate());
    }
}
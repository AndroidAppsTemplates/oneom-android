package com.iam.oneom.env.compoundviews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.iam.oneom.R;
import com.iam.oneom.core.entities.Episode;
import com.iam.oneom.core.entities.Torrent;
import com.iam.oneom.core.util.Decorator;
import com.iam.oneom.env.widget.text.Text;
import com.iam.oneom.env.widget.text.font;

import java.util.ArrayList;

public class EpisodeInfoWindow extends LinearLayout {

    Episode episode;

    Text serialName;
    FrameLayout header;
    FrameLayout headerShadow;
    Text episodeName;
    Text airdate;
    Text torrentsHeader;
    ArrayList<Text> torrents;

    public EpisodeInfoWindow(Context context) {
        super(context);
        init();
    }

    public EpisodeInfoWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        setHeaderView();
        setEpisodeNameView();
        setAirdateView();
        setTorrentsHeaderView();
        setBackgroundResource(R.drawable.episode_window_background);
    }

    public void setData(Episode episode) {
        this.episode = episode;
        serialName.setText(episode.serial().title());
        this.airdate.setText(episode.airdate());
        String text = episode.title() + " " + episode.episodeInSeason();
        episodeName.setText(text);
        setTorrentsView(episode);
    }

    private void setTorrentsView(final Episode episode) {
        if (torrents == null || torrents.size() == 0) {
            torrents = new ArrayList<>();
            for (Torrent torrent : episode.torrent()) {
                Text text = new Text(getContext());
                MarginLayoutParams params = new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT);
                text.setLayoutParams(params);
                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                text.setPadding(16, 0, 0, 0);
                text.setText(torrent.tagInfo());
                text.setTextColor(Decorator.TXTLINKBLUE);
                torrents.add(text);
                addView(text);
            }
        }
    }

    private void setTorrentsHeaderView() {
        torrentsHeader = new Text(getContext());
        MarginLayoutParams params = new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT);
        torrentsHeader.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        torrentsHeader.setTypeface(font.font133sb.typeface());
        torrentsHeader.setTextColor(Decorator.BLACK);
        torrentsHeader.setPadding(16, 8, 16, 16);
        torrentsHeader.setLayoutParams(params);
        torrentsHeader.setText("Torrents");
        addView(torrentsHeader);
    }

    private void setAirdateView() {
        airdate = new Text(getContext());
        MarginLayoutParams params = new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT);
        airdate.setPadding(16, 8, 16, 8);
        airdate.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        airdate.setTextColor(Decorator.BLACK);
        airdate.setLayoutParams(params);
        addView(airdate);
    }

    private void setEpisodeNameView() {
        episodeName = new Text(getContext());
        MarginLayoutParams params = new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT);
        episodeName.setTypeface(font.font133.typeface());
        episodeName.setTextColor(Decorator.BLACK);
        episodeName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        episodeName.setPadding(16, 32, 16, 8);
        episodeName.setLayoutParams(params);
        addView(episodeName);
    }

    private void setHeaderView() {
        header = new FrameLayout(getContext());
        header.setLayoutParams(new MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, MarginLayoutParams.WRAP_CONTENT));
        header.setBackgroundResource(R.drawable.episode_window_header_backround);

//        header.setBackgroundColor(Decorator.BGGREEN);
        serialName = new Text(getContext());
        serialName.setTextColor(Decorator.BLACK);
        MarginLayoutParams params = new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT);
        serialName.setTypeface(font.font133sb.typeface());
        serialName.setPadding(16,16,16,16);
        serialName.setLayoutParams(params);
        header.addView(serialName);
        addView(header);
        headerShadow = new FrameLayout(getContext());
        headerShadow.setBackgroundColor(Decorator.WINDOW_HEADER_STROKE_COLOR);
        headerShadow.setLayoutParams(new MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, (int)TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics())));
        addView(headerShadow);
    }
}

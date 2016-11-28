package com.iam.oneom.pages.main;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

import com.iam.oneom.R;

public class PlayerActivity extends AppCompatActivity {

    VideoView player;
    String link;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player_activity);

        link = getIntent().getStringExtra(getString(R.string.episode_player_intent));
        MediaController mc = new MediaController(this);
        mc.setAnchorView(mc);


        player = (VideoView) findViewById(R.id.video);
        Uri uri = Uri.parse(link);
        player.setVideoURI(uri);
        player.setMediaController(mc);

        player.start();
    }
}

package com.iam.oneom.pages.main.EpisodePage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.iam.oneom.R;
import com.iam.oneom.core.entities.Util;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.util.Decorator;
import com.iam.oneom.env.widget.ToolbarAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class EpisodePageActivityNew extends AppCompatActivity {

    private static final String TAG = EpisodePageActivityNew.class.getSimpleName();
    private static final String EP_ID_EXTRA = "EP_ID_EXTRA";

    Episode episode;

    public static void open(Context context, long epId) {
        Intent intent = new Intent(context, EpisodePageActivityNew.class);
        intent.putExtra(EP_ID_EXTRA, epId);
        context.startActivity(intent);
    }

    @BindView(R.id.poster)
    ImageView imagePoster;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bluring_area)
    FrameLayout blurArea;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.episode_page_activity);
        ButterKnife.bind(this);
        long id = getIntent().getLongExtra(EP_ID_EXTRA, 0);
        episode = Realm.getDefaultInstance().where(Episode.class).equalTo("id", id).findFirst();
        configureBackground(Util.posterUrl(episode));
    }

    private void configureBackground(String url) {
        ToolbarAdapter adapter = new ToolbarAdapter(toolbar);

        adapter.configureToolbarColorSheme(episode);
        adapter.configureText(episode);

        blurArea.setBackgroundColor(adapter.tint());

        Glide
                .with(this)
                .load(url)
                .asBitmap()
                .centerCrop()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        int averageColorInt = Decorator.getAverageColorInt(resource);


                        int tintColor = 0xf0000000 + averageColorInt % 0x1000000;

                        Realm.getDefaultInstance().executeTransaction(realm -> Util.storePosterTint(episode, tintColor));

                        Decorator.setStatusBarColor(EpisodePageActivityNew.this, Util.posterTint(episode));
                        blurArea.setBackgroundColor(tintColor);


                        adapter.configureToolbarColorSheme(episode);

                        return false;
                    }
                })
                .into(new BitmapImageViewTarget(imagePoster) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        Bitmap bitmap = Decorator.fastblur(resource, 1, 50);

                        imagePoster.setImageBitmap(bitmap);
//                        Blurer.applyBlur(new FullScreenBlurArea(blurArea));
                    }
                });
    }
}

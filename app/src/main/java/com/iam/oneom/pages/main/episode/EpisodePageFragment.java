package com.iam.oneom.pages.main.episode;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.iam.oneom.R;
import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.DbUtil;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.rx.EpisodeDataReceivedEvent;
import com.iam.oneom.core.rx.RxBus;
import com.iam.oneom.core.util.Decorator;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;


public class EpisodePageFragment extends Fragment {

    private static final String ID_EXTRA = "ID_EXTRA";

    @BindView(R.id.posterImage)
    public ImageView smallPoster;
    @BindView(R.id.description)
    public TextView description;
    @BindView(R.id.desc_title)
    public TextView descTitle;

    @BindColor(R.color.light)
    protected int lightColor;
    @BindColor(R.color.dark)
    protected int darkColor;
    @BindColor(R.color.middle)
    protected int middleColor;

    @BindDimen(R.dimen.ep_poster_corner_radius)
    public int cornerRadius;

    private View view;

    private Episode episode;

    public static EpisodePageFragment getFragment(long id) {
        EpisodePageFragment fragment = new EpisodePageFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ID_EXTRA, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.episode_page_episode_page, container, false);
        ButterKnife.bind(this, view);

        setData();
        configureViews();
        return view;
    }

    private void setData() {
        long id = getArguments().getLong(ID_EXTRA);
        episode = DbHelper.where(Episode.class).equalTo("id", id).findFirst();
        RxBus.INSTANCE.register(EpisodeDataReceivedEvent.class,
                episodeDataReceivedEvent -> {
                    this.episode = episodeDataReceivedEvent.getEpisode();
                    description.setText(
                            Html.fromHtml(
                                    DbUtil.description(episode, getString(R.string.no_description)
                                    )
                            )
                    );
                });
    }

    private void configureViews() {

        if (episode == null) {
            return;
        }

        if (episode.getDescription() != null && episode.getDescription().size() > 0) {
            description.setText(
                    Html.fromHtml(
                            DbUtil.description(episode, getString(R.string.no_description)
                            )
                    )
            );
        }

        descTitle.setText(String.format("%s%s", DbUtil.episodeInSeasonString(episode), episode.getTitle() == null ? "" : (" " + episode.getTitle())));

        Glide.with(this)
                .load(DbUtil.posterUrl(episode, Decorator.W480))
                .asBitmap()
                .centerCrop()
                .error(R.drawable.ic_movie_dark_24dp)
                .into(new BitmapImageViewTarget(smallPoster) {
                    @Override
                    protected void setResource(Bitmap resource) {

                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCornerRadius(cornerRadius);

                        smallPoster.setImageDrawable(circularBitmapDrawable);
                    }
                });

    }
}
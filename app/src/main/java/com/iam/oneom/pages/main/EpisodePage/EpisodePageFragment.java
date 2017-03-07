package com.iam.oneom.pages.main.EpisodePage;

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
import com.iam.oneom.core.entities.Util;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.network.Web;
import com.iam.oneom.core.network.response.EpResponse;
import com.iam.oneom.core.rx.EpisodeImageTintDefinedEvent;
import com.iam.oneom.core.rx.RxBus;
import com.iam.oneom.core.util.Decorator;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EpisodePageFragment extends Fragment implements Callback<EpResponse> {

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

        if (episode != null) {
            configureViews();
        }


        RxBus.INSTANCE.register(EpisodeImageTintDefinedEvent.class,
                episodeImageReceivedEvent -> {
                    configureColorSheme(episodeImageReceivedEvent.getTint());
                });

        return view;
    }

    private void setData() {
        long id = getArguments().getLong(ID_EXTRA);
        episode = DbHelper.where(Episode.class).equalTo("id", id).findFirst();
        Web.instance.getEpisode(id).enqueue(this);
    }

    private void configureViews() {

        description.setText(
                Html.fromHtml(
                        Util.description(episode, getString(R.string.no_description)
                        )
                )
        );

        Glide.with(this)
                .load(Util.posterUrl(episode, Decorator.W480))
                .asBitmap()
                .centerCrop()
                .error(R.drawable.ic_movie_black_48dp)
                .into(new BitmapImageViewTarget(smallPoster) {
                    @Override
                    protected void setResource(Bitmap resource) {

                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCornerRadius(cornerRadius);

                        smallPoster.setImageDrawable(circularBitmapDrawable);
                    }
                });

        configureColorSheme(Decorator.pureColor(Util.posterTint(episode)));
    }

    private void configureColorSheme(int tint) {

        int textColor = Decorator.pureColor(tint) > middleColor ? darkColor : lightColor;

        description.setTextColor(textColor);
        descTitle.setTextColor(textColor);
    }

    @Override
    public void onResponse(Call<EpResponse> call, Response<EpResponse> response) {
        this.episode = response.body().getEpisode();
        configureViews();
    }

    @Override
    public void onFailure(Call<EpResponse> call, Throwable t) {

    }
}
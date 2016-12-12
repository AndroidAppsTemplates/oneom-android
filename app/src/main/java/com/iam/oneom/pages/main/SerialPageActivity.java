package com.iam.oneom.pages.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iam.oneom.R;
import com.iam.oneom.core.entities.Util;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.entities.model.Serial;
import com.iam.oneom.core.entities.model.Torrent;
import com.iam.oneom.core.network.Web;
import com.iam.oneom.core.util.Decorator;
import com.iam.oneom.core.util.Editor;
import com.iam.oneom.core.util.Time;
import com.iam.oneom.env.handling.recycler.BindableViewHolder;
import com.iam.oneom.env.handling.recycler.itemdecorations.SpacesBetweenItemsDecoration;
import com.iam.oneom.env.handling.recycler.layoutmanagers.GridLayoutManager;
import com.iam.oneom.env.widget.CircleProgressBar;
import com.iam.oneom.env.widget.TagBar;
import com.iam.oneom.env.widget.svg;
import com.iam.oneom.env.widget.text.Text;
import com.iam.oneom.pages.main.EpisodePage.EpisodePageActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class SerialPageActivity extends AppCompatActivity {

    long serial_id;
    Serial serial;

    @BindView(R.id.poster)
    ImageView posterImage;
    @BindView(R.id.progress)
    CircleProgressBar cpb;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    GridLayoutManager layoutManager;
    SerialAdapter serialAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_page_activity);

        ButterKnife.bind(this);

        serial_id = getIntent().getLongExtra(getString(R.string.media_page_serial_intent), 0);

        showProgressBar();
        Web.instance.getSerial(serial_id)
                .subscribe(s -> {
                    Realm.getDefaultInstance().executeTransaction(realm -> realm.insertOrUpdate(s.getSerial()));
                    this.serial = s.getSerial();
                    loadBackground(Util.posterUrl(serial));
                    configureRecycler();
                    hideProgressBar();
                });
    }

    class SerialAdapter extends RecyclerView.Adapter<BindableViewHolder> implements Refresheable {

        private static final int HEADER = 0;
        private static final int ITEM = 1;
        private static final int SEASON_SELECTOR = 2;

        private int selected;

        LayoutInflater inflater;

        SerialAdapter(Context context) {
            selected = 1;
            inflater = ((Activity) context).getLayoutInflater();
        }

        @Override
        public BindableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case HEADER:
                    return new SerialHeaderVH(inflater.inflate(R.layout.media_page_header, parent, false));
                case SEASON_SELECTOR:
                    return new SeasonSelectorVH(inflater.inflate(R.layout.media_page_serial_season_number_item, parent, false));
                case ITEM:
                    return new EpisodeVH(inflater.inflate(R.layout.episodes_list_item, parent, false));
            }
            throw new RuntimeException("EpisodePageActivity.SerialAdapter has not view type with tag" + viewType);
        }

        @Override
        public void onBindViewHolder(BindableViewHolder holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return 2 + Util.episodesCountForSeason(serial, selected);
        }

        @Override
        public int getItemViewType(int position) {
            if (isHeader(position)) {
                return HEADER;
            } else if (isSeasonNumber(position)) {
                return SEASON_SELECTOR;
            } else {
                return ITEM;
            }
        }

        public boolean isSeasonNumber(int position) {
            return  (position == 1);
        }

        public boolean isHeader(int position) {
            return position == 0;
        }

        @Override
        public void refresh() {
            serialAdapter.notifyDataSetChanged();
        }

        class SeasonSelectorVH extends BindableViewHolder {

            View view;
            @BindView(R.id.pager)
            ViewPager pager;

            public SeasonSelectorVH(View itemView) {
                super(itemView);
                view = itemView;
                ButterKnife.bind(this, view);
            }

            @Override
            public void onBind(int position) {
                pager.setAdapter(new SeasonNumberAdapter(getSupportFragmentManager()));
                pager.setCurrentItem(selected);
                pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        selected = position + 1;
                    }
                });
            }

            class SeasonNumberAdapter extends FragmentPagerAdapter {

                public SeasonNumberAdapter(FragmentManager fm) {
                    super(fm);
                }

                @Override
                public Fragment getItem(int position) {
                    SeasonNumberFragment seasonNumberFragment = new SeasonNumberFragment();
                    seasonNumberFragment.setNumber(position + 1);
                    seasonNumberFragment.setCallback(SerialAdapter.this);
                    return seasonNumberFragment;
                }

                @Override
                public int getCount() {
                    return Util.seasonsCountForSerial(serial);
                }
            }
        }

        class SerialHeaderVH extends BindableViewHolder {

            private TextView serialName;
            private RecyclerView infoRecyclerView;
            private GridLayoutManager gridLayoutManager;
            private InfoAdapter infoAdapter;
            private View view;
            private int viewType;
            private ArrayList<String> info = new ArrayList<>();

            public SerialHeaderVH(View itemView) {
                super(itemView);
                this.view = itemView;
                serialName = (TextView) view.findViewById(R.id.serialname);
                infoRecyclerView = (RecyclerView) view.findViewById(R.id.mediainfo);
                infoRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onBind(int position) {
                serialName.setText(serial.getTitle());
                if (infoRecyclerView.getAdapter() == null) {
                    infoAdapter = new InfoAdapter(SerialPageActivity.this);
                    gridLayoutManager = new GridLayoutManager(SerialPageActivity.this, 10);
                    infoRecyclerView.setAdapter(infoAdapter);
                    infoRecyclerView.setLayoutManager(gridLayoutManager);
                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override public int getSpanSize(int position) {
                            return (position < 5) ? 2 : 5;
                        }
                    });
                }
            }

            class InfoAdapter extends RecyclerView.Adapter<BindableViewHolder> {

                LayoutInflater inflater;

                private static final int INFO = 1;
                private static final int HYPERLINK = 2;

                public InfoAdapter(Context context) {
                    inflater = ((AppCompatActivity) context).getLayoutInflater();
                }

                @Override
                public BindableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    if (viewType == INFO) {
                        return new InfoVH(inflater.inflate(R.layout.media_page_serial_header_info_item, parent, false));
                    } else {
                        return new HyperlinkVH(inflater.inflate(R.layout.media_page_serial_header_hyperlink_item, parent, false));
                    }
                }

                @Override
                public void onBindViewHolder(BindableViewHolder holder, int position) {
                    holder.onBind(position);
                }

                @Override
                public int getItemCount() {
                    return 13;
                }

                @Override
                public int getItemViewType(int position) {
                    return position >= 5 ? INFO : HYPERLINK;
                }

                class InfoVH extends BindableViewHolder {

                    TextView title;
                    TextView value;

                    public InfoVH(View itemView) {
                        super(itemView);
                        title = (TextView) itemView.findViewById(R.id.title);
                        value = (TextView) itemView.findViewById(R.id.value);
                    }

                    @Override
                    public void onBind(int position) {
                        switch (position) {
                            case 5:
                                title.setText("IMDB");
                                value.setText(serial.getImdbRating());
                                break;
                            case 6:
                                title.setText("Runtime");
                                value.setText(String.valueOf(serial.getRuntime()));
                                break;
                            case 7:
                                title.setText("Genre");
                                value.setText(Editor.namesByComma(serial.getGenre()));
                                break;
                            case 8:
                                title.setText("Network");
                                value.setText(Editor.namesByComma(serial.getNetwork()));
                                break;
                            case 9:
                                title.setText("Country");
                                value.setText(Editor.namesByComma(serial.getCountry()));
                                break;
                            case 10:
                                title.setText("Status");
                                value.setText(Util.serialStatusName(serial));
                                break;
                            case 11:
                                title.setText("Air Start");
                                value.setText(Time.format(serial.getStart(), Time.TimeFormat.IDN));
                                break;
                            case 12:
                                title.setText("Air End");
                                value.setText(Time.format(serial.getEnd(), Time.TimeFormat.IDN));
                                break;
                        }
                    }
                }

                class HyperlinkVH extends BindableViewHolder {

                    Text hyperlink;
                    String id;

                    public HyperlinkVH(View itemView) {
                        super(itemView);
                        hyperlink = (Text) itemView.findViewById(R.id.hyperlink);
                    }

                    @Override
                    public void onBind(int position) {
                        switch (position) {
                            case 0:
                                hyperlink.setText("TVDB");
                                break;
                            case 1:
                                hyperlink.setText("TNDb");
                                break;
                            case 2:
                                hyperlink.setText("TVRAGE");
                                break;
                            case 3:
                                hyperlink.setText("TVMAZE");
                                break;
                            case 4:
                                hyperlink.setText("VK Group");
                                break;
                        }
                    }
                }
            }

        }

        class EpisodeVH extends BindableViewHolder {

            View view;
            ImageView image;
            Text title;
            TagBar tagbar;
            FrameLayout frameLayout;

            public EpisodeVH(View itemView) {
                super(itemView);
                view = itemView;
                view.setBackgroundResource(R.drawable.table_item_border);
                setImage(itemView);
                setTitle(itemView);
                tagbar = (TagBar) itemView.findViewById(R.id.tagBar);
            }

            private void setTitle(View itemView) {
                title = (Text) itemView.findViewById(R.id.title);
                title.setTextColor(Decorator.TXTBLUE);
            }

            private void setImage(View itemView) {
                image = (ImageView) itemView.findViewById(R.id.image);
                frameLayout = (FrameLayout) itemView.findViewById(R.id.imageframe);
                Decorator.setSquareSize(frameLayout, Decorator.getSizeForTable(3) - (int) Decorator.dipToPixels(SerialPageActivity.this, 8) * 2);
                image.setBackgroundResource(R.drawable.episode_item_image_cropper);
            }

            @Override
            public void onBind(final int position) {

                final Episode ep = Util.episodesForSeasonList(serial, selected).get(position - 2);// = episodes.get(position - 1);
                String titleText = ep.getTitle() + " " + Util.episodeInSeasonString(ep);
                title.setText(titleText);
                ArrayList<String> tags = new ArrayList<>();
                for (Torrent torrent : ep.getTorrent()) {
                    tags.add(Util.qualityTag(torrent));
                }
                tagbar.addTags(tags);
                Glide
                        .with(view.getContext())
                        .load(Util.posterUrl(ep))
                        .into(image);
                view.setOnClickListener(v -> {
                    Intent intent = new Intent(getApplicationContext(), EpisodePageActivity.class);
                    intent.putExtra(getString(R.string.media_page_episode_intent), ep.getId());
                    startActivity(intent);
                });
            }
        }

    }

    private void configureRecycler() {
        serialAdapter = new SerialAdapter(this);
        recycler.setAdapter(serialAdapter);
        layoutManager = new GridLayoutManager(this, 15);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                if (serialAdapter.isHeader(position)) {
                    return 13;
                }

                if (serialAdapter.isSeasonNumber(position)) {
                    return 2;
                }

                return 5;
            }
        });
        recycler.addItemDecoration(new SpacesBetweenItemsDecoration((int) Decorator.dipToPixels(this, 8)));
        recycler.setLayoutManager(layoutManager);
    }

    private void loadBackground(String url) {
        Glide
                .with(this)
                .load(url)
                .into(posterImage);
    }

    private void showProgressBar() {
        cpb.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        cpb.setVisibility(View.INVISIBLE);
    }
}

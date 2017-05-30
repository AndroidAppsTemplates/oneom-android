package com.iam.oneom.pages.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.iam.oneom.R;
import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.model.Serial;
import com.iam.oneom.core.network.Web;
import com.iam.oneom.env.handling.recycler.BindableViewHolder;
import com.iam.oneom.env.handling.recycler.itemdecorations.SpacesBetweenItemsDecoration;
import com.iam.oneom.env.handling.recycler.layoutmanagers.GridLayoutManager;
import com.iam.oneom.env.widget.CircleProgressBar;
import com.iam.oneom.pages.main.serial.EpisodeVH;
import com.iam.oneom.util.Decorator;
import com.iam.oneom.util.Editor;
import com.iam.oneom.util.OneOmUtil;
import com.iam.oneom.util.Time;

import java.util.Date;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SerialPageActivity extends AppCompatActivity {
    private static final String TAG = SerialPageActivity.class.getSimpleName();
    private static final String SERIAL_ID_EXTRA = "SERIAL_ID_EXTRA";

    long serial_id;
    Serial serial;

    @BindView(R.id.poster)
    ImageView posterImage;
    @BindView(R.id.progress)
    CircleProgressBar cpb;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.bluring_area)
    FrameLayout bluringArea;

    private int selected = 1;

    @BindDimen(R.dimen.serial_page_items_spacing)
    int serialPageItemsSpacing;

    GridLayoutManager layoutManager;
    SerialAdapter serialAdapter;

    public static final void start(Context context, long id) {
        Intent intent = new Intent(context, SerialPageActivity.class);
        intent.putExtra(SERIAL_ID_EXTRA, id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_page_activity);

        ButterKnife.bind(this);

        serial_id = getIntent().getLongExtra(SERIAL_ID_EXTRA, 0);

        showProgressBar();
        Web.instance.getSerial(serial_id)
                .subscribe(serialResponse -> {
                    DbHelper.insert(serialResponse.getSerial());
                    this.serial = serialResponse.getSerial();
                    loadBackground(OneOmUtil.posterUrl(serial, Decorator.W480));
                    configureRecycler();
                    hideProgressBar();
                });
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
        recycler.addItemDecoration(new SpacesBetweenItemsDecoration(serialPageItemsSpacing));
        recycler.setLayoutManager(layoutManager);
    }


    class SerialAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Refresheable {

        private static final int HEADER = 0;
        private static final int ITEM = 1;
        private static final int SEASON_SELECTOR = 2;

        LayoutInflater inflater;

        SerialAdapter(Context context) {
            inflater = ((Activity) context).getLayoutInflater();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case HEADER:
                    return new SerialHeaderVH(inflater.inflate(R.layout.media_page_header_serial, parent, false));
                case SEASON_SELECTOR:
                    return new SeasonSelectorVH(inflater.inflate(R.layout.media_page_serial_season_number_item, parent, false));
                case ITEM:
                    return new EpisodeVH(inflater.inflate(R.layout.episodes_list_item, parent, false));
            }
            throw new RuntimeException("EpisodePageActivity.SerialAdapter has not recyclerView type with tag" + viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof EpisodeVH) {
                ((EpisodeVH) holder).onBind(OneOmUtil.episodesForSeasonList(serial, selected).get(position - 2));
            } else if (holder instanceof SerialHeaderVH) {
                ((SerialHeaderVH) holder).onBind();
            } else if (holder instanceof SeasonSelectorVH) {
                ((SeasonSelectorVH) holder).onBind();
            }
        }

        @Override
        public int getItemCount() {
            return 2 + OneOmUtil.episodesCountForSeason(serial, selected);
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
            return (position == 1);
        }

        public boolean isHeader(int position) {
            return position == 0;
        }

        @Override
        public void refresh() {
            serialAdapter.notifyItemRangeChanged(2, OneOmUtil.episodesCountForSeason(serial, selected));
        }

        class SerialHeaderVH extends RecyclerView.ViewHolder {

            public static final String IMDB = "IMDB";
            public static final String RUNTIME = "Runtime";
            public static final String GENRE = "Genre";
            public static final String NETWORK = "Network";
            public static final String COUNTRY = "Country";
            public static final String STATUS = "Status";
            public static final String AIR_START = "Air Start";
            public static final String AIR_END = "Air End";

            public static final String TVDB = "TVDB";
            public static final String TNDb = "TNDb";
            public static final String TVRAGE = "TVRAGE";
            public static final String TVMAZE = "TVMAZE";
            public static final String VK_GROUP = "VK Group";

            String[] linkItems = new String[]{
                    TVDB,
                    TNDb,
                    TVRAGE,
                    TVMAZE,
                    VK_GROUP
            };

            String[] items = new String[]{
                    IMDB,
                    RUNTIME,
                    GENRE,
                    NETWORK,
                    COUNTRY,
                    STATUS,
                    AIR_START,
                    AIR_END
            };

            @BindView(R.id.serialname)
            protected TextView serialName;
            @BindView(R.id.mediainfo)
            protected RecyclerView infoRecyclerView;
            private GridLayoutManager gridLayoutManager;
            private InfoAdapter infoAdapter;

            public SerialHeaderVH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            public void onBind() {
                serialName.setText(serial.getTitle());
                if (infoRecyclerView.getAdapter() == null) {
                    infoAdapter = new InfoAdapter(SerialPageActivity.this);
                    gridLayoutManager = new GridLayoutManager(SerialPageActivity.this, 10);
                    infoRecyclerView.setAdapter(infoAdapter);
                    infoRecyclerView.setLayoutManager(gridLayoutManager);
                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            return (position < linkItems.length) ? 2 : linkItems.length;
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
                    return linkItems.length + items.length;
                }

                @Override
                public int getItemViewType(int position) {
                    return position >= linkItems.length ? INFO : HYPERLINK;
                }

                class InfoVH extends BindableViewHolder {

                    @BindView(R.id.title)
                    TextView title;
                    @BindView(R.id.value)
                    TextView value;

                    public InfoVH(View itemView) {
                        super(itemView);
                        ButterKnife.bind(this, itemView);
                    }

                    @Override
                    public void onBind(int position) {

                        String key = items[position - linkItems.length];
                        title.setText(key);

                        switch (key) {
                            case IMDB:
                                value.setText(serial.getImdbRating());
                                break;
                            case RUNTIME:
                                value.setText(String.valueOf(serial.getRuntime()));
                                break;
                            case GENRE:
                                value.setText(Editor.namesByComma(serial.getGenre()));
                                break;
                            case NETWORK:
                                value.setText(Editor.namesByComma(serial.getNetwork()));
                                break;
                            case COUNTRY:
                                value.setText(Editor.namesByComma(serial.getCountry()));
                                break;
                            case STATUS:
                                value.setText(OneOmUtil.serialStatusName(serial));
                                break;
                            case AIR_START:
                                value.setText(Time.format(new Date(serial.getStart()), Time.TimeFormat.IDN));
                                break;
                            case AIR_END:
                                value.setText(Time.format(new Date(serial.getEnd()), Time.TimeFormat.IDN));
                                break;
                        }
                    }
                }

                class HyperlinkVH extends BindableViewHolder {

                    TextView hyperlink;
                    String id;

                    public HyperlinkVH(View itemView) {
                        super(itemView);
                        hyperlink = (TextView) itemView.findViewById(R.id.hyperlink);
                    }

                    @Override
                    public void onBind(int position) {
                        hyperlink.setText(linkItems[position]);
                    }
                }
            }
        }

        class SeasonSelectorVH extends RecyclerView.ViewHolder {

            @BindView(R.id.pager)
            ViewPager pager;
            @BindView(R.id.prev)
            ImageView prev;
            @BindView(R.id.next)
            ImageView next;


            public SeasonSelectorVH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                pager.setAdapter(new SeasonNumberAdapter(getSupportFragmentManager()));
                prev.setOnClickListener(v -> pager.setCurrentItem(selected - 1 < 1 ? 0 : selected - 2));
                next.setOnClickListener(v -> {
                    int seasonsCount = OneOmUtil.seasonsCountForSerial(serial);
                    pager.setCurrentItem(selected + 1 >= seasonsCount ? seasonsCount - 1 : selected);
                });
            }

            public void onBind() {
                pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        selected = position + 1;
                        serialAdapter.refresh();
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
                    seasonNumberFragment.setCallback(serialAdapter);
                    return seasonNumberFragment;
                }

                @Override
                public int getCount() {
                    return OneOmUtil.seasonsCountForSerial(serial);
                }
            }
        }
    }

    private void loadBackground(String url) {
        Glide
                .with(this)
                .load(url)
                .asBitmap()
                .centerCrop()
                .into(new BitmapImageViewTarget(posterImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        posterImage.setImageBitmap(Decorator.fastblur(resource, 1, 50));
                        bluringArea.setBackgroundColor(Decorator.setTransparencyPercent(92, Decorator.getAverageColorInt(resource)));
                    }
                });
    }

    private void showProgressBar() {
        cpb.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        cpb.setVisibility(View.INVISIBLE);
    }
}

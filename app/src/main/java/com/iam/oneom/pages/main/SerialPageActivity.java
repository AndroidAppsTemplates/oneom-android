package com.iam.oneom.pages.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.iam.oneom.R;
import com.iam.oneom.core.entities.Util;
import com.iam.oneom.core.entities.model.Serial;
import com.iam.oneom.core.network.Web;
import com.iam.oneom.core.util.Decorator;
import com.iam.oneom.core.util.Editor;
import com.iam.oneom.core.util.Time;
import com.iam.oneom.env.handling.recycler.BindableViewHolder;
import com.iam.oneom.env.handling.recycler.itemdecorations.SpacesBetweenItemsDecoration;
import com.iam.oneom.env.handling.recycler.layoutmanagers.GridLayoutManager;
import com.iam.oneom.env.widget.CircleProgressBar;
import com.iam.oneom.env.widget.blur.Blurer;
import com.iam.oneom.env.widget.blur.FullScreenBlurArea;
import com.iam.oneom.env.widget.text.Text;

import java.util.ArrayList;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class SerialPageActivity extends AppCompatActivity {
    private static final String TAG  = SerialPageActivity.class.getSimpleName();

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

    @BindDimen(R.dimen.serial_page_items_spacing)
            int serialPageItemsSpacing;

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

        private int selected;

        LayoutInflater inflater;

        SerialAdapter(Context context) {
            selected = 1;
            inflater = ((Activity) context).getLayoutInflater();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof EpisodeVH) {
                ((EpisodeVH) holder).onBind(Util.episodesForSeasonList(serial, selected).get(position - 2));
            } else if (holder instanceof SerialHeaderVH) {
                ((SerialHeaderVH) holder).onBind();
            } else if (holder instanceof SeasonSelectorVH) {
                ((SeasonSelectorVH) holder).onBind();
            }
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
//            serialAdapter.notifyDataSetChanged();
        }

        class SeasonSelectorVH extends RecyclerView.ViewHolder {

            View view;
            @BindView(R.id.pager)
            ViewPager pager;

            public SeasonSelectorVH(View itemView) {
                super(itemView);
                view = itemView;
                ButterKnife.bind(this, view);
            }

            public void onBind() {
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

        class SerialHeaderVH extends RecyclerView.ViewHolder {

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

            public void onBind() {
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

                        int averageColorInt = Decorator.getAverageColorInt(resource);


                        bluringArea.setBackgroundColor(0xE0000000 + averageColorInt);

                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(posterImage.getContext().getResources(), resource);
                        posterImage.setImageDrawable(circularBitmapDrawable);
                        Blurer.applyBlur(new FullScreenBlurArea(bluringArea));

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

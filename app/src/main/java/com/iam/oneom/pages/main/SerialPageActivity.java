package com.iam.oneom.pages.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

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
import com.iam.oneom.env.handling.recycler.layoutmanagers.LinearLayoutManager;
import com.iam.oneom.env.widget.CircleProgressBar;
import com.iam.oneom.env.widget.TagBar;
import com.iam.oneom.env.widget.svg;
import com.iam.oneom.env.widget.text.Text;
import com.iam.oneom.env.widget.text.font;
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

    class SerialAdapter extends RecyclerView.Adapter<BindableViewHolder> {

        private static final int HEADER = 0;
        private static final int ITEM = 1;
        private static final int SEASON_NUMBER = 2;

        boolean[] expandedSeasons;

        LayoutInflater inflater;

        SerialAdapter(Context context) {
            int episodesCountForSerial = Util.seasonsCountForSerial(serial);
            expandedSeasons = new boolean[episodesCountForSerial];
            for (int i = 0, l = episodesCountForSerial; i < l; i++) {
                expandedSeasons[i] = false;
            }
            inflater = ((Activity) context).getLayoutInflater();
        }

        @Override
        public BindableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case HEADER:
                    return new SerialHeaderVH(inflater.inflate(R.layout.media_page_header, parent, false));
                case SEASON_NUMBER:
                    return new SeasonNumberVH(inflater.inflate(R.layout.media_page_serial_season_number_item, parent, false));
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
            int res = 1; // header by default
            for (int i = 0, l = expandedSeasons.length; i < l; i++) {
                if (expandedSeasons[i])
                    res += Util.episodesForSeasonList(serial, i + 1).size(); // if expanded go through the episodes view holders
                res += 1; // as view holder for serial number is always here
            }

            return res;
        }

        @Override
        public int getItemViewType(int position) {
            if (isHeader(position)) {
                return HEADER;
            } else if (isSeasonNumber(position)) {
                return SEASON_NUMBER;
            } else {
                return ITEM;
            }
        }

        public boolean isSeasonNumber(int position) {
            if (position == 1) return true;

            int pointer = 1; // header + first season number header: by default

            for (int i = 1, l = expandedSeasons.length; i <= l; i++) {

                if (pointer < position) {
                    pointer += 1; // as view holder for serial number is always here
                    // if expanded - go through the episodes view holders
                    if (expandedSeasons[i - 1]) {
                        pointer += Util.episodesForSeasonList(serial, i).size();
                    }
                    if (pointer == position) return true;
                }
            }

            return false;
        }

        public boolean isHeader(int position) {
            return position == 0;
        }

        class SeasonNumberVH extends BindableViewHolder {

            View view;
            Text text;
            ImageView icon;

            public SeasonNumberVH(View itemView) {
                super(itemView);
                view = itemView;
                text = (Text) itemView.findViewById(R.id.season);
                icon = (ImageView) itemView.findViewById(R.id.icon);
                icon.setImageDrawable(svg.down.drawable());
            }

            @Override
            public void onBind(int position) {

                int res = seasonNumberAtPosition(position);

                setOnClickListener(res);

                text.setText("Season " + res);

            }

            public void setOnClickListener(final int pos) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        expandedSeasons[pos - 1] = !expandedSeasons[pos - 1];
                        SerialAdapter.this.notifyDataSetChanged();
                    }
                });
            }

            private int seasonNumberAtPosition(int position) {
                int res = 0;
                int before = position;
                int after = position;
                for (int i = 0, l = expandedSeasons.length; i < l; i++) {
                    if (expandedSeasons[i]) {
                        after -= Util.episodesForSeasonList(serial, i + 1).size();
                    }
                    if (after < 0) res = before;
                    else before = after;
                }

                if (after > 0) res = after;
                return res;
            }


        }

        class SerialHeaderVH extends BindableViewHolder {

            private Text serialName;
            private RecyclerView infoRecyclerView;
            private LinearLayoutManager linearLayoutManager;
            private InfoAdapter infoAdapter;
            private View view;
            private int viewType;
            private ArrayList<String> info = new ArrayList<>();

            public SerialHeaderVH(View itemView) {
                super(itemView);
                this.view = itemView;
                serialName = (Text) view.findViewById(R.id.serialname);
                infoRecyclerView = (RecyclerView) view.findViewById(R.id.mediainfo);
                infoRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onBind(int position) {
                serialName.setText(serial.getTitle());
                if (infoRecyclerView.getAdapter() == null) {
                    infoAdapter = new InfoAdapter(SerialPageActivity.this);
                    linearLayoutManager = new LinearLayoutManager(SerialPageActivity.this);
                    infoRecyclerView.setAdapter(infoAdapter);
                    infoRecyclerView.setLayoutManager(linearLayoutManager);
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
                    return position < 8 ? INFO : HYPERLINK;
                }

                class InfoVH extends BindableViewHolder {

                    Text title;
                    Text value;

                    public InfoVH(View itemView) {
                        super(itemView);
                        title = (Text) itemView.findViewById(R.id.title);
                        value = (Text) itemView.findViewById(R.id.value);
                    }

                    @Override
                    public void onBind(int position) {
                        switch (position) {
                            case 0:
                                title.setText("IMDB");
                                value.setText(serial.getImdbRating());
                                break;
                            case 1:
                                title.setText("Runtime");
                                value.setText(String.valueOf(serial.getRuntime()));
                                break;
                            case 2:
                                title.setText("Genre");
                                value.setText(Editor.namesByComma(serial.getGenre()));
                                break;
                            case 3:
                                title.setText("Network");
                                value.setText(Editor.namesByComma(serial.getNetwork()));
                                break;
                            case 4:
                                title.setText("Country");
                                value.setText(Editor.namesByComma(serial.getCountry()));
                                break;
                            case 5:
                                title.setText("Status");
                                value.setText(Util.serialStatusName(serial));
                                break;
                            case 6:
                                title.setText("Air Start");
                                value.setText(Time.format(serial.getStart(), Time.TimeFormat.IDN));
                                break;
                            case 7:
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
                            case 8:
                                hyperlink.setText("TVDB");
                                break;
                            case 9:
                                hyperlink.setText("TNDb");
                                break;
                            case 10:
                                hyperlink.setText("TVRAGE");
                                break;
                            case 11:
                                hyperlink.setText("TVMAZE");
                                break;
                            case 12:
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
                title.setTypeface(font.font133sb.typeface(SerialPageActivity.this));
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

                final Episode ep = episodeAtPosition(position);// = episodes.get(position - 1);
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

            private Episode episodeAtPosition(int position) {

                int epNumber = 0;
                int season = 0;
                int before = position - 1;  // exclude header from calculation
                int after = position - 1;   // both of this pointers for surf table elements


                for (int i = 0, l = expandedSeasons.length; i < l; i++) {


                    after--; // exclude season number view holder

                    // find incomplete season
                    if (expandedSeasons[i]) {
                        after -= Util.episodesForSeasonList(serial, i + 1).size();
                    }

                    // if find - set params
                    if (after < 0) {
                        epNumber = before - 1;
                        season = i + 1;
                        break;
                    }

                    // if not - going ahead
                    else before = after;
                }

                return Util.episodesForSeasonList(serial, season).get(epNumber);
            }
        }

    }

    private void configureRecycler() {
        serialAdapter = new SerialAdapter(this);
        recycler.setAdapter(serialAdapter);
        layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return serialAdapter.isHeader(position) || serialAdapter.isSeasonNumber(position) ? layoutManager.getSpanCount() : 1;
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

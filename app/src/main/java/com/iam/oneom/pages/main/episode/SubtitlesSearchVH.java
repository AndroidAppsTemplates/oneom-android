package com.iam.oneom.pages.main.episode;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iam.oneom.R;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.search.Key;
import com.iam.oneom.core.util.Decorator;
import com.iam.oneom.core.util.Time;
import com.iam.oneom.core.util.Web;
import com.iam.oneom.env.handling.recycler.BindableViewHolder;
import com.iam.oneom.env.handling.recycler.itemdecorations.SpacesBetweenItemsDecoration;
import com.iam.oneom.env.handling.recycler.layoutmanagers.LinearLayoutManager;
import com.iam.oneom.env.widget.CircleProgressBar;
import com.iam.oneom.env.widget.text.Text;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class SubtitlesSearchVH extends BindableViewHolder {

    private Episode episode;
    private String searchString;

    private int selectedSubtitleSourcePosition, selectedSubtitleLanguagePosition;

    private Context context;

    private CircleProgressBar cpb;

    private View view;

    private TextView selectSource;
    private TextView selectLang;

    private ListPopupWindow spw, lpw;

    private RecyclerView searchResults;
    private SubtitlesSearchAdapter adapter;
    private LinearLayoutManager manager;

    public SubtitlesSearchVH(View itemView, CircleProgressBar cpb, Episode episode) {
        super(itemView);
        this.episode = episode;
        this.searchString = episode.getSerial().getTitle();
        this.cpb = cpb;
        this.view = itemView;
        this.context = view.getContext();

        spw = new ListPopupWindow(context);
        lpw = new ListPopupWindow(context);

        selectSource = (TextView) itemView.findViewById(R.id.source);
        selectLang = (TextView) itemView.findViewById(R.id.lang);
        searchResults = (RecyclerView) view.findViewById(R.id.searchResults);
        manager = new LinearLayoutManager(context);
        adapter = new SubtitlesSearchAdapter(context);
        searchResults.addItemDecoration(new SpacesBetweenItemsDecoration((int) Decorator.dipToPixels(context, 8)));
        searchResults.setLayoutManager(manager);
        searchResults.setAdapter(adapter);

        setTexts();
        setClickListeners();

    }

    private void setClickListeners() {

//        selectSource.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Decorator.configurePopup(selectSource, spw, new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View recyclerView, int position, long id) {
//                        selectedSubtitleSourcePosition = position;
//                        spw.dismiss();
//                        resetViewHolder();
//                    }
//                }, Source.names(Source.Type.Subtitle));
//            }
//        });

//        selectLang.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Decorator.configurePopup(selectLang, lpw, new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View recyclerView, int position, long id) {
//                        selectedSubtitleLanguagePosition = position;
//                        lpw.dismiss();
//                        resetViewHolder();
//                    }
//                }, Lang.names());
//            }
//        });
    }

    private void resetViewHolder() {
        setTexts();
    }

    private void setTexts() {
//        selectSource.setText(Source.getByType(Source.Type.Subtitle, selectedSubtitleSourcePosition).getName());
//        selectLang.setText(Lang.lang(selectedSubtitleLanguagePosition).getName());
    }

    @Override
    public void onBind(int position) {

    }

    class SubtitlesSearchAdapter extends RecyclerView.Adapter<BindableViewHolder> {

        private static final int SUBTITLE = 1;
        private static final int FOOTER = 2;

        List<HashMap<Key, String>> list;
        HashMap<Key, String> headers;
        LayoutInflater inflater;

        private SubtitlesSearchAdapter(Context context) {
            list = new ArrayList<>();
            headers = new HashMap<>();
            headers.put(Key.Name, "Title");
            headers.put(Key.Uploaded, "Uploaded");
            headers.put(Key.Language, "Lang");
            headers.put(Key.Download, "Download");

//            reloadData(currentOrigin());

            inflater = ((AppCompatActivity)context).getLayoutInflater();
        }

        @Override
        public BindableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case SUBTITLE:
                    return new SubtitleVH(inflater.inflate(R.layout.media_page_episode_search_item_subtitle, parent, false));
                case FOOTER:
                    return new FooterVH(inflater.inflate(R.layout.media_page_episode_search_footer, parent, false));
            }
            throw new RuntimeException(this.getClass().getName() + " has no recyclerView holder with type " + viewType);
        }

        @Override
        public void onBindViewHolder(BindableViewHolder holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return 0;//list.size() <= 1 ? 1 : list.size() + 1;
        }

//        public void reloadData(Source.Origin origin) {
//            list = Search.instance().results(searchString, origin);
//        }

        class SubtitleVH extends BindableViewHolder {

            TextView title;
            TextView uploaded;
            TextView lang;

            TextView download;

            View view;


            public SubtitleVH(View itemView) {
                super(itemView);

                this.view = itemView;
                title = (TextView) itemView.findViewById(R.id.title);
                uploaded = (TextView) itemView.findViewById(R.id.uploaded);
                lang = (TextView) itemView.findViewById(R.id.lang);
                download = (TextView) itemView.findViewById(R.id.download);
            }

            @Override
            public void onBind(int position) {
                final HashMap<Key, String> data;
                if (position == 0) {
                    data = headers;
                    title.setGravity(Gravity.CENTER);
                    download.setTextColor(Color.TRANSPARENT);
                } else {
                    data = list.get(position - 1);

                    download.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(data.get(Key.Download)));
                            view.getContext().startActivity(i);
                        }
                    });

                    title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    uploaded.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

                    try {
                        uploaded.setText(Time.format(data.get(Key.Uploaded), Time.TimeFormat.IDTwTZ, Time.TimeFormat.OutputDT));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        uploaded.setText("unknown date");
                    }

                    title.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Web.openUrlInWebView(context, data.get(Key.Page));
                        }
                    });
                }
                title.setText(data.get(Key.Name));

                lang.setText(data.get(Key.Language));
            }
        }

        class FooterVH extends BindableViewHolder {

            Text search;

            public FooterVH(View itemView) {
                super(itemView);
                search = (Text) itemView.findViewById(R.id.search);
//                search.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.d("url source", Source.getByType(Source.Type.Subtitle, selectedSubtitleSourcePosition).searchPage());
//                        Search.engine(cpb, new Search.OnSearchListener() {
//                            @Override
//                            public void onSearchResult() {
//                                adapter.reloadData(currentOrigin());
//                                adapter.notifyDataSetChanged();
//                            }
//                        }, episode, Lang.lang(selectedSubtitleLanguagePosition)).find(Source.getByType(Source.Type.Subtitle, selectedSubtitleSourcePosition), searchString);
//                    }
//                });
            }

            @Override
            public void onBind(int position) {
                search.setText("Search Subtitles");
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position == list.size() ? FOOTER : SUBTITLE;
        }
    }

//    private Source.Origin currentOrigin() {
//        return Source.getByType(Source.Type.Subtitle, selectedSubtitleSourcePosition).origin();
//    }
}
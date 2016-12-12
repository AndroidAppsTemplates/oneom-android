package com.iam.oneom.pages.main.EpisodePage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.iam.oneom.R;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.search.Key;
import com.iam.oneom.core.search.Search;
import com.iam.oneom.core.util.Decorator;
import com.iam.oneom.core.util.Web;
import com.iam.oneom.env.handling.recycler.BindableViewHolder;
import com.iam.oneom.env.handling.recycler.layoutmanagers.LinearLayoutManager;
import com.iam.oneom.env.widget.CircleProgressBar;
import com.iam.oneom.env.widget.text.Text;
import com.iam.oneom.env.widget.text.font;

import java.util.HashMap;
import java.util.List;

class TorrentSearchVH extends BindableViewHolder implements Search.OnSearchListener {

    private Episode episode;
    private String searchString;

    private int selectedTorrentSourcePosition, selectedQualityGroupPosition = 1, selectedLanguagePosition;
    private Context context;

    private CircleProgressBar cpb;

    private ListPopupWindow tpw;
    private ListPopupWindow qgpw;
    private ListPopupWindow lpw;

    private Text selectTracker;
    private Text selectQualityGroup;
    private Text selectLang;

    private RecyclerView searchResults;
    private TorrentsListAdapter adapter;
    private LinearLayoutManager manager;

    public TorrentSearchVH (View view, CircleProgressBar cpb, Episode episode) {
        super(view);
        this.cpb = cpb;
        this.episode = episode;
        this.searchString = episode.getSerial().getTitle();
        this.context = view.getContext();
        tpw = new ListPopupWindow(view.getContext());
        qgpw = new ListPopupWindow(view.getContext());
        lpw = new ListPopupWindow(view.getContext());
        selectTracker = (Text) view.findViewById(R.id.tracker);
        selectQualityGroup = (Text) view.findViewById(R.id.quality);
        selectLang = (Text) view.findViewById(R.id.lang);
        searchResults = (RecyclerView) view.findViewById(R.id.searchResults);
        manager = new LinearLayoutManager(context);
        adapter = new TorrentsListAdapter();
        searchResults.setLayoutManager(manager);
        searchResults.setAdapter(adapter);

//        setTexts();
        setClickListeners();
    }

    private void setClickListeners() {

//        selectTracker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Decorator.configurePopup(selectTracker, tpw, new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        selectedTorrentSourcePosition = position;
//                        Search.instance().clearResults();
//                        tpw.dismiss();
//                        resetViewHolder();
//                    }
//                }, Source.names(Source.Type.Torrent));
//            }
//        });

//        selectQualityGroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Decorator.configurePopup(selectQualityGroup, qgpw, new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        selectedQualityGroupPosition = position;
//                        qgpw.dismiss();
//                        Search.instance().clearResults();
//                        resetViewHolder();
//                    }
//                }, QualityGroup.names());
//            }
//        });

//        selectLang.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Decorator.configurePopup(selectLang, lpw, new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        selectedLanguagePosition = position;
//                        lpw.dismiss();
//                        Search.instance().clearResults();
//                        resetViewHolder();
//                    }
//                }, Lang.names());
//            }
//        });
    }

//    private void resetViewHolder() {
//        setTexts();
//    }

//    private void setTexts() {
//        selectTracker.setText(Source.getByType(Source.Type.Torrent, selectedTorrentSourcePosition).getName());
//        selectQualityGroup.setText(QualityGroup.group(selectedQualityGroupPosition).getName());
//        selectLang.setText(Lang.lang(selectedLanguagePosition).getName());
//    }

    @Override
    public void onBind(int position) {

    }

    @Override
    public void onSearchResult() {
//        adapter.reloadData(currentOrigin());
        adapter.notifyDataSetChanged();
    }

    class TorrentsListAdapter extends RecyclerView.Adapter<BindableViewHolder> {

        private static final int TORRENT = 1;
        private static final int TORRENT_SEARCH_FOOTER = 2;

        List<HashMap<Key, String>> list;
        HashMap<Key, String> headers;
        LayoutInflater inflater;

        public TorrentsListAdapter() {
            headers = new HashMap<>();
            headers.put(Key.Name, "Title");
            headers.put(Key.Seeds, "S");
            headers.put(Key.Leachs, "L");
            headers.put(Key.Size, "Size");
//            list = Search.instance().results(searchString, currentOrigin());
            inflater = ((AppCompatActivity)context).getLayoutInflater();
        }

//        public void reloadData(Source.Origin origin) {
//            list = Search.instance().results(searchString, origin);
//        }

        @Override
        public BindableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case TORRENT:
                    return new TorrentVH(inflater.inflate(R.layout.media_page_episode_search_item_torrent, parent, false));
                case TORRENT_SEARCH_FOOTER:
                    return new FooterVH(inflater.inflate(R.layout.media_page_episode_search_footer, parent, false));
            }

            throw new RuntimeException("TorrentsListAdapter viewType with unknown identifier " + viewType);
        }

        @Override
        public void onBindViewHolder(BindableViewHolder holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return 0;// list.size() == 0 ? 1 : list.size() + 2;
        }

        class TorrentVH extends BindableViewHolder {

            Text title;
            Text seeds;
            Text leachs;
            Text size;
            Text download;

            TorrentVH(View view) {
                super(view);
                title = (Text) view.findViewById(R.id.title);
                seeds = (Text) view.findViewById(R.id.seeds);
                leachs = (Text) view.findViewById(R.id.leachs);
                size = (Text) view.findViewById(R.id.size);
                download = (Text) view.findViewById(R.id.download);
            }

            @Override
            public void onBind(int position) {
                if (list.size() > 0) {
                    final HashMap<Key, String> data;

                    if (position == 0) {
                        data = headers;
                        title.setGravity(Gravity.CENTER);
                        title.setTextStyle(font.font133b);
                        size.setTextStyle(font.font133b);
                        download.setTextColor(Color.TRANSPARENT);
                    } else {
                        data = list.get(position - 1);

                        download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(data.get(Key.Download)));
                                if (context.getPackageManager().queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY).size() > 0) {
                                    context.startActivity(Intent.createChooser(i, context.getString(R.string.torrent_client_select_dialog_title)));
                                } else {
                                    Toast.makeText(context, "You must install torrent client on your device", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        title.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Web.openUrlInWebView(context, data.get(Key.Page));
                            }
                        });
                    }

                    title.setText(data.get(Key.Name));
                    seeds.setText(data.get(Key.Seeds));
                    leachs.setText(data.get(Key.Leachs));
                    size.setText(data.get(Key.Size));
                }
            }
        }

        class FooterVH extends BindableViewHolder {

            Text search;

            public FooterVH(View itemView) {
                super(itemView);
                search = (Text) itemView.findViewById(R.id.search);
//                itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Search.engine(
//                                cpb,
//                                TorrentSearchVH.this,
//                                episode,
//                                Lang.lang(selectedLanguagePosition),
//                                QualityGroup.qualityGroup(selectedQualityGroupPosition)
//                        ).find(Source.getByType(Source.Type.Torrent, selectedTorrentSourcePosition), searchString);
//                    }
//                });
            }

            @Override
            public void onBind(int position) {
                if (position == 0) search.setText("Search torrents");
                else search.setText("Find more");
            }
        }

        @Override
        public int getItemViewType(int position) {
            return TorrentsListAdapter.this.isFooter(position) ? TORRENT_SEARCH_FOOTER : TORRENT;
        }

        private boolean isFooter(int position) {
            if (list.size() == 0) return position == 0;
            else return position == list.size() + 1;
        }
    }

//    private Source.Origin currentOrigin() {
//        return Source.getByType(Source.Type.Torrent, selectedTorrentSourcePosition).origin();
//    }
}
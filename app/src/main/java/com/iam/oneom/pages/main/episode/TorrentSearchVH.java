package com.iam.oneom.pages.main.episode;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.iam.oneom.R;
import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.Util;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.entities.model.Lang;
import com.iam.oneom.core.entities.model.QualityGroup;
import com.iam.oneom.core.entities.model.Source;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;

class TorrentSearchVH extends BindableViewHolder implements Search.OnSearchListener {

    private Episode episode;
    private String searchString;

    private int selectedTorrentSourcePosition, selectedQualityGroupPosition = 1, selectedLanguagePosition;

    private CircleProgressBar cpb;

    private ListPopupWindow tpw;
    private ListPopupWindow qgpw;
    private ListPopupWindow lpw;

    @BindView(R.id.tracker)
    TextView selectTracker;
    @BindView(R.id.quality)
    TextView selectQualityGroup;
    @BindView(R.id.lang)
    TextView selectLang;
    @BindView(R.id.searchResults)
    RecyclerView searchResults;
    private TorrentsListAdapter adapter;
    private LinearLayoutManager manager;

    public TorrentSearchVH(View view, CircleProgressBar cpb, Episode episode) {
        super(view);

        ButterKnife.bind(this, view);

        this.cpb = cpb;
        this.episode = episode;
        this.searchString = episode.getSerial().getTitle();

        tpw = new ListPopupWindow(view.getContext());
        qgpw = new ListPopupWindow(view.getContext());
        lpw = new ListPopupWindow(view.getContext());

        manager = new LinearLayoutManager(view.getContext());
        adapter = new TorrentsListAdapter(view.getContext());
        searchResults.setLayoutManager(manager);
        searchResults.setAdapter(adapter);

        selectQualityGroup.setText(getTorrentQG().get(1).getName());
        selectTracker.setText(getTorrentSources().get(0).getName());
        selectLang.setText(getTorrentLangs().get(0).getName());
    }

    @OnClick(R.id.lang)
    public void selectLang(View view) {
        Decorator.configurePopup(selectLang, lpw, (parent, v, position, id) -> {
            selectedLanguagePosition = position;
            lpw.dismiss();
//                        Search.instance().clearResults();
            selectLang.setText(getTorrentLangs().get(selectedLanguagePosition).getShortName());
        }, Util.langNames(getTorrentLangs()));
    }

    @OnClick(R.id.quality)
    public void selectQG(View view) {
        Decorator.configurePopup(selectQualityGroup, qgpw, (parent, v, position, id) -> {
            selectedQualityGroupPosition = position;
            qgpw.dismiss();
//                        Search.instance().clearResults();
            selectQualityGroup.setText(getTorrentQG().get(selectedQualityGroupPosition).getName());

        }, Util.qgNames(getTorrentQG()));
    }

    @OnClick(R.id.tracker)
    public void selectTracker(View view) {
        Decorator.configurePopup(selectTracker, tpw, (parent, v, position, id) -> {
            selectedTorrentSourcePosition = position;
            Search.instance().clearResults();
            tpw.dismiss();
            selectTracker.setText(getTorrentSources().get(selectedTorrentSourcePosition).getName());
        }, Util.sourceNames(getTorrentSources()));
    }

    @NonNull
    private RealmResults<Lang> getTorrentLangs() {
        return DbHelper.where(Lang.class).findAll();
    }

    @NonNull
    private RealmResults<Source> getTorrentSources() {
        return DbHelper.where(Source.class).equalTo("typeId", Source.Type.Torrent.type).findAll();
    }

    @NonNull
    private RealmResults<QualityGroup> getTorrentQG() {
        return DbHelper.where(QualityGroup.class).findAll();
    }

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

        public TorrentsListAdapter(Context context) {
            headers = new HashMap<>();
            headers.put(Key.Name, "Title");
            headers.put(Key.Seeds, "S");
            headers.put(Key.Leachs, "L");
            headers.put(Key.Size, "Size");
//            list = Search.instance().results(searchString, currentOrigin());
            inflater = ((AppCompatActivity) context).getLayoutInflater();
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
                                if (v.getContext().getPackageManager().queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY).size() > 0) {
                                    v.getContext().startActivity(Intent.createChooser(i, v.getContext().getString(R.string.torrent_client_select_dialog_title)));
                                } else {
                                    Toast.makeText(v.getContext(), "You must install torrent client on your device", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        title.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Web.openUrlInWebView(v.getContext(), data.get(Key.Page));
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
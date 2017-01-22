package com.iam.oneom.pages.main.EpisodePage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iam.oneom.R;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.search.Key;
import com.iam.oneom.core.search.Search;
import com.iam.oneom.core.util.Decorator;
import com.iam.oneom.core.util.Web;
import com.iam.oneom.env.handling.recycler.BindableViewHolder;
import com.iam.oneom.env.handling.recycler.itemdecorations.SpacesBetweenItemsDecoration;
import com.iam.oneom.env.handling.recycler.layoutmanagers.GridLayoutManager;
import com.iam.oneom.env.widget.CircleProgressBar;
import com.iam.oneom.env.widget.text.Text;

import java.util.ArrayList;
import java.util.HashMap;

class OnlineSearchVH extends BindableViewHolder {

    private Episode episode;
    private String searchString;

    private int selectedOnlineSourcePosition, selectedOnlineLanguagePosition;

    private Context context;

    private CircleProgressBar cpb;

    private View view;

    private TextView selectSource;
    private TextView selectLang;

    private ListPopupWindow spw, lpw;

    private RecyclerView searchResults;
    private OnlineSearchAdapter adapter;
    private GridLayoutManager manager;

    public OnlineSearchVH(View itemView, CircleProgressBar cpb, Episode episode) {
        super(itemView);
        this.episode = episode;
        this.searchString = episode.getSerial().getTitle();
        this.cpb = cpb;
        this.view = itemView;
        this.context = view.getContext();

        spw = new ListPopupWindow(context);
        lpw = new ListPopupWindow(context);

        selectSource = (TextView) itemView.findViewById(R.id.source);
        selectLang= (TextView) itemView.findViewById(R.id.lang);
        searchResults = (RecyclerView) view.findViewById(R.id.searchResults);
        manager = new GridLayoutManager(context, 3);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.isFooter(position) ? manager.getSpanCount() : 1;
            }
        });
        adapter = new OnlineSearchAdapter();
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
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        selectedOnlineSourcePosition = position;
//                        spw.dismiss();
//                        resetViewHolder();
//                    }
//                }, Source.names(Source.Type.Online));
//            }
//        });

//        selectLang.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Decorator.configurePopup(selectLang, lpw, new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        selectedOnlineLanguagePosition = position;
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
//        selectSource.setText(Source.getByType(Source.Type.Online, selectedOnlineSourcePosition).getName());
//        selectLang.setText(Lang.lang(selectedOnlineLanguagePosition).getName());
    }

    @Override
    public void onBind(int position) {

    }

    private class OnlineSearchAdapter extends RecyclerView.Adapter<BindableViewHolder> {

        private static final int ONLINE_ITEM = 0;
        private static final int FOOTER_ITEM = 1;

        private LayoutInflater inflater;

        private ArrayList<HashMap<Key, String>> list;

        public OnlineSearchAdapter() {
            inflater = ((AppCompatActivity)context).getLayoutInflater();
            list = new ArrayList<>();
            HashMap<Key, String> d = new HashMap<>();
//            for (Online o : episode.online()) {
//                d.put(Key.Name, o.title());
//                d.put(Key.VideoLink, o.videoUrl());
//                d.put(Key.PosterUrl, o.posterURL());
//                d.put(Key.Page, o.url());
//                list.add(d);
//            }
        }

        @Override
        public BindableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case ONLINE_ITEM:
                    return new OnlineVH(inflater.inflate(R.layout.media_page_episode_search_item_online, parent, false));
                case FOOTER_ITEM:
                    return new FooterVH(inflater.inflate(R.layout.media_page_episode_search_footer, parent, false));
            }

            throw new RuntimeException("OnlineSearchAdapter hasn't view type vith value " + viewType);
        }

//        public void reloadData(Source.Origin origin) {
//            list = Search.instance().results(searchString, origin);
//        }

        @Override
        public void onBindViewHolder(BindableViewHolder holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return 0;//list.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            return isFooter(position) ? FOOTER_ITEM : ONLINE_ITEM;
        }

        private boolean isFooter(int position) {
            return position == list.size();
        }

        private class OnlineVH extends BindableViewHolder {

            View view;

            ImageView videopageLink;
            ImageView posterImage;
            FrameLayout imageHolder;
            Text title;
            Button playButton;

            public OnlineVH(View itemView) {
                super(itemView);
                view = itemView;
                imageHolder = (FrameLayout) itemView.findViewById(R.id.imageframe);
                videopageLink = (ImageView) view.findViewById(R.id.pagelink);
                posterImage = (ImageView) view.findViewById(R.id.posterimage);
                imageHolder.setLayoutParams(
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                (int) Decorator.dipToPixels(context, 200))
                );
                title = (Text) view.findViewById(R.id.title);
                playButton = (Button) view.findViewById(R.id.playbutton);
            }

            public void onBind(int position) {
                final HashMap<Key, String> data = list.get(position);
                videopageLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Web.openUrlInWebView(context, data.get(Key.Page));
                    }
                });
                Glide
                        .with(context)
                        .load(data.get(Key.PosterUrl))
                        .into(posterImage);
                title.setText(data.get(Key.Name));
                playButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (data.get(Key.VideoLink) != null) {
                            playVideo(Uri.parse(data.get(Key.VideoLink)));
                            new AsyncTask<String, Void, Void>() {

                                @Override
                                protected Void doInBackground(String... params) {
                                    String ep_id = params[0];
                                    String url = params[1];
                                    HashMap<String, String> data = new HashMap<>();
                                    data.put("ep_id", ep_id);
                                    data.put("url", url);
                                    Web.POST(Web.url.domain + Web.url.videoIndex, data);
                                    return null;
                                }
                            }.execute(episode.getId() + "", data.get(Key.VideoLink));
                        } else {
                            new AsyncTask<Void, Void, Void>() {

                                @Override
                                protected void onPreExecute() {
                                    cpb.setVisibility(View.VISIBLE);
                                }

                                @Override
                                protected Void doInBackground(Void... params) {
//                                    data.put(Key.VideoLink, Online.extractVideoLink(
//                                            Web.GET(
//                                                    data.get(Key.Page), false
//                                            ),
//                                            currentOrigin()
//                                    ));
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    cpb.setVisibility(View.INVISIBLE);
                                    playVideo(Uri.parse(data.get(Key.VideoLink)));
                                }
                            }.execute();
                        }
                    }
                });
            }

            private void playVideo(Uri uri) {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setDataAndType(uri, "video/*");
                context.startActivity(intent);
            }
        }

        private class FooterVH extends BindableViewHolder {

            private Text search;

            public FooterVH(View itemView) {
                super(itemView);
                this.search = (Text) itemView.findViewById(R.id.search);
//                search.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Search.engine(cpb, new Search.OnSearchListener() {
//                            @Override
//                            public void onSearchResult() {
////                                adapter.reloadData(currentOrigin());
//                                adapter.notifyDataSetChanged();
//                            }
//                        }, episode, Lang.lang(selectedOnlineLanguagePosition)).find(Source.getByType(Source.Type.Online, selectedOnlineSourcePosition), searchString);
//                    }
//                });
            }

            public void onBind(int position) {
                if (list.size() == 0) {
                    search.setText("Search Video Online");
                } else  {
                    search.setText("Find More...");
                }
            }
        }
    }

//    private Source.Origin currentOrigin() {
//        return Source.getByType(Source.Type.Online, selectedOnlineSourcePosition).origin();
//    }
}
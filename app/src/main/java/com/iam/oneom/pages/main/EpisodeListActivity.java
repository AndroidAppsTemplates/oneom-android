package com.iam.oneom.pages.main;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.iam.oneom.R;
import com.iam.oneom.core.entities.*;
import com.iam.oneom.core.util.Decorator;
import com.iam.oneom.core.util.Editor;
import com.iam.oneom.core.util.Web;
import com.iam.oneom.env.handling.recycler.BindableViewHolder;
import com.iam.oneom.env.handling.recycler.itemdecorations.EqualSpaceItemDecoration;
import com.iam.oneom.env.handling.recycler.layoutmanagers.GridLayoutManager;
import com.iam.oneom.env.widget.CircleProgressBar;
import com.iam.oneom.env.widget.TagBar;
import com.iam.oneom.env.widget.svg;
import com.iam.oneom.env.widget.text.Text;
import com.iam.oneom.env.widget.text.font;
import com.iam.oneom.pages.OneOm;
import com.iam.oneom.pages.main.EpisodePage.EpisodePageActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EpisodeListActivity extends AppCompatActivity {

    private CircleProgressBar progressBar;

    private InitTask startupTask;

    private String data;
    private String staticData;

    ListPopupWindow popupWindow;

    EditText searchET;
    ImageView searchIcon;
    RelativeLayout searchView;

    ArrayList<Episode> episodes;
    RecyclerView episodesGrid;
    GridLayoutManager recyclerLayoutManager;
    EpisodesAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.episodes_list_activity);

        searchView = (RelativeLayout) findViewById(R.id.searchHeader);
        searchIcon = (ImageView) findViewById(R.id.search_icon);
        searchIcon.setImageDrawable(svg.search.drawable());
        searchET = (EditText) findViewById(R.id.search_et);
        searchET.addTextChangedListener(new TextWatcher() {

            AsyncTask<String, Void, String> task;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                cancelStartupTask();
                if (task != null) {
                    hideProgressBar();
                    task.cancel(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (s != null && s.length() > 0) {
                    adapter.filterOnSearch(Editor.splitTOWords(s.toString()));
                    task = new AsyncTask<String, Void, String>() {
                        @Override
                        protected void onPreExecute() {
                            showProgressBar();
                        }

                        @Override
                        protected String doInBackground(String... params) {
                            String searchString = null;
                            try {
                                searchString = URLEncoder.encode(params[0], "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                OneOm.handleError(Thread.currentThread(), e, "in serial names for entered text task doInBackground");
                                e.printStackTrace();
                            }
                            String result = Web.GET(Web.url.domain + Web.url.serial + Web.url.search + "/" + searchString, true);

                            return result;
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            hideProgressBar();
                            try {
                                JSONArray serials = new JSONObject(s).getJSONArray("serials");
                                int l = serials.length();
                                LinkedHashMap<String, String> ss = new LinkedHashMap<>();
                                for (int i = 0; i < l; i++) {
                                    JSONObject serial = serials.getJSONObject(i);
                                    ss.put(serial.getString("title"), serial.getString("id"));
                                }

                                showPopup(ss);
                            } catch (JSONException e) {
                                OneOm.handleError(Thread.currentThread(), e, "in serial names for entered text task onPostExecute");
                                e.printStackTrace();
                            }
                        }
                    }.execute(s.toString());
                } else {
                    adapter.filterOnSearch(new ArrayList<String>());
                }
            }
        });

        startupTask = new InitTask();

//        Decorator.configureActionBar(this);

        Decorator.init(this);
        progressBar = (CircleProgressBar) findViewById(R.id.progress);
        episodesGrid = (RecyclerView) findViewById(R.id.mainrecycler);
        episodesGrid.addItemDecoration(new EqualSpaceItemDecoration((int)Decorator.dipToPixels(this, 5)));

        staticData = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.main_static_entities), "");
        data = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.main_episode_list), "");

        if (!data.equals("")) {
            initStaticEntities(staticData);
            makeEpisodesList(data);
            invalidateRecycler();
        }
        startupTask.execute();
    }

    class EpisodesAdapter extends RecyclerView.Adapter<BindableViewHolder> {

        public static final int SEARCH = 0;
        public static final int EPISODE = 1;

        LayoutInflater inflater;
        ArrayList<Episode> episodes;

        public EpisodesAdapter(ArrayList<Episode> episodes, AppCompatActivity context) {
            if (context != null) inflater = context.getLayoutInflater();
            this.episodes = episodes;
        }

        @Override
        public BindableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case EPISODE:
                    return new EpisodeVH(inflater.inflate(R.layout.episodes_list_item, parent, false));
                case SEARCH:
                    return new SearchVH(inflater.inflate(R.layout.episodes_list_header, parent, false));
            }
            throw new RuntimeException("EpisodesAdapter in EpisodeListActivity has not item with view type " + viewType);
        }

        @Override
        public void onBindViewHolder(BindableViewHolder holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemViewType(int position) {
//            if (isHeader(position)) {
//                return SEARCH;
//            } else {
                return EPISODE;
//            }
        }

        public void filterOnSearch(ArrayList<String> matchStrings) {
            showProgressBar();

            HashMap<Episode, Integer> epsWithMatchCount = new HashMap<>();
            ArrayList<Episode> resultEpisodesList = new ArrayList<>();

            for (Episode e : EpisodeListActivity.this.episodes) {
                int containsCount = 0;
                for (String s : matchStrings) {
                    if (e.serial().title().toLowerCase().contains(s.toLowerCase())) {
                        containsCount++;
                    }
                }

                epsWithMatchCount.put(e, containsCount);
            }

            for (int i = matchStrings.size(); i > 0; i--) {
                for (Map.Entry<Episode, Integer> entry : epsWithMatchCount.entrySet()) {
                    if (entry.getValue() == i) {
                        resultEpisodesList.add(entry.getKey());
                    }
                }
            }

            if (resultEpisodesList.size() == 0) {
                EpisodesAdapter.this.episodes = EpisodeListActivity.this.episodes;
            } else {
                EpisodesAdapter.this.episodes = resultEpisodesList;
            }

            notifyDataSetChanged();

//            final SearchVH svh = (SearchVH) episodesGrid.findViewHolderForLayoutPosition(0);
  //          if (svh != null) {
                /*svh.*/requestEditTextFocus();
    //        }

            hideProgressBar();
        }

        public boolean isHeader(int position) {
            return position == 0;
        }

        @Override
        public int getItemCount() {
            return episodes.size();// + 1;
        }

        class SearchVH extends BindableViewHolder {

            ImageView icon;
            EditText editText;

            View view;

            public SearchVH (View view) {
                super(view);
                this.view = view;
                icon = (ImageView) view.findViewById(R.id.search_icon);
                icon.setImageDrawable(svg.search.drawable());
                editText = (EditText) view.findViewById(R.id.search_et);
                editText.addTextChangedListener(new TextWatcher() {

                    AsyncTask<String, Void, String> task;

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        cancelStartupTask();
                        if (task != null) {
                            hideProgressBar();
                            task.cancel(true);
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                    }

                    @Override
                    public void afterTextChanged(final Editable s) {
                        if (s != null && s.length() > 0) {
                            adapter.filterOnSearch(Editor.splitTOWords(s.toString()));
                            task = new AsyncTask<String, Void, String>() {
                                @Override
                                protected void onPreExecute() {
                                    showProgressBar();
                                }

                                @Override
                                protected String doInBackground(String... params) {
                                    String searchString = null;
                                    try {
                                        searchString = URLEncoder.encode(params[0], "UTF-8");
                                    } catch (UnsupportedEncodingException e) {
                                        OneOm.handleError(Thread.currentThread(), e, "in serial names for entered text task doInBackground");
                                        e.printStackTrace();
                                    }
                                    String result = Web.GET(Web.url.domain + Web.url.serial + Web.url.search + "/" + searchString, true);

                                    return result;
                                }

                                @Override
                                protected void onPostExecute(String s) {
                                    hideProgressBar();
                                    try {
                                        JSONArray serials = new JSONObject(s).getJSONArray("serials");
                                        int l = serials.length();
                                        LinkedHashMap<String, String> ss = new LinkedHashMap<>();
                                        for (int i = 0; i < l; i++) {
                                            JSONObject serial = serials.getJSONObject(i);
                                            ss.put(serial.getString("title"), serial.getString("id"));
                                        }

                                        showPopup(ss);
                                    } catch (JSONException e) {
                                        OneOm.handleError(Thread.currentThread(), e, "in serial names for entered text task onPostExecute");
                                        e.printStackTrace();
                                    }
                                }
                            }.execute(s.toString());
                        } else {
                            adapter.filterOnSearch(new ArrayList<String>());
                        }
                    }
                });
            }

            public void showPopup(final LinkedHashMap<String, String> serials) {
                if (serials != null && serials.size() > 0) {

                    popupWindow = new ListPopupWindow(EpisodeListActivity.this);

                    final ArrayList<String> names = new ArrayList<>(serials.size());
                    ArrayList<String> ids = new ArrayList<>(serials.size());
                    for (Map.Entry<String, String> entry : serials.entrySet()) {
                        names.add(entry.getKey());
                        ids.add(entry.getValue());
                    }

                    PopupAdapter adapter = new PopupAdapter(EpisodeListActivity.this, R.layout.popup_item, names, ids);

                    popupWindow.setAdapter(adapter);
                    popupWindow.setModal(false);
                    popupWindow.setWidth((int) (Decorator.getScreenWidth() * 0.9));
                    popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                    popupWindow.setAnchorView(SearchVH.this.view);
                    popupWindow.show();
                }

                requestEditTextFocus();
            }

            public void requestEditTextFocus() {
                SearchVH.this.editText.post(new Runnable() {
                    public void run() {
                        SearchVH.this.editText.requestFocusFromTouch();
                        InputMethodManager lManager = (InputMethodManager) EpisodeListActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        lManager.showSoftInput(SearchVH.this.editText, 0);
                    }
                });
            }

            @Override
            public void onBind(int position) {
                view.setTag(adapter);
            }

        }

        class EpisodeVH extends BindableViewHolder {

            View view;
            ImageView image;
            Text title;
            TagBar tagbar;
            RelativeLayout frame;
            FrameLayout frameLayout;

            public EpisodeVH(View itemView) {
                super(itemView);
                view = itemView;
                view.setBackgroundResource(R.drawable.table_item_border);
                setFrame(itemView);
                setImage(itemView);
                setTitle(itemView);
                setTagBar(itemView);
            }

            private void setFrame(View itemView) {
                frame = (RelativeLayout) itemView.findViewById(R.id.frame);
                int a = (int)Decorator.dipToPixels(EpisodeListActivity.this, 8);
                frame.setPadding(a, a, a, a);
            }

            private void setTagBar(View itemView) {
                tagbar = (TagBar) itemView.findViewById(R.id.tagBar);
            }

            private void setTitle(View itemView) {
                title = (Text) itemView.findViewById(R.id.title);
                title.setTypeface(font.font133sb.typeface());
                title.setTextColor(Decorator.TXTBLUE);
            }

            private void setImage(View itemView) {
                image = (ImageView) itemView.findViewById(R.id.image);
                frameLayout = (FrameLayout) itemView.findViewById(R.id.imageframe);
                Decorator.setSquareSize(frameLayout, Decorator.getSizeForTable(3) - (int)Decorator.dipToPixels(EpisodeListActivity.this, 8) * 2);
                image.setBackgroundResource(R.drawable.episode_item_image_cropper);
            }

            @Override
            public void onBind(final int position) {
                final Episode ep = episodes.get(position);
                String titleText = ep.serial().title() + " " + ep.episodeInSeason();
                title.setText(titleText);
                ArrayList<String> tags = new ArrayList<>();
                for (Torrent torrent : ep.torrent()) {
                    tags.add(torrent.tagInfo());
                }
                tagbar.addTags(tags);
                Glide
                        .with(view.getContext())
                        .load(ep.posterURL())
                        .into(image);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        cancelStartupTask();

                        Intent intent = new Intent(getApplicationContext(), EpisodePageActivity.class);
                        intent.putExtra(getString(R.string.media_page_episode_intent), episodes.get(position));
                        startActivity(intent);
                    }
                });
            }
        }
    }

    class PopupAdapter extends ArrayAdapter<String> {

        List<String> names;
        List<String> ids;
        LayoutInflater inflater;
        int resource;

        public PopupAdapter(Context context, int resource, List<String> objects, List<String> ids) {
            super(context, resource, objects);
            inflater = ((AppCompatActivity)context).getLayoutInflater();
            this.ids = ids;
            this.names = objects;
            this.resource = resource;
        }


        @Override
        public int getCount() {
            return names.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            SearchResultViewHolder holder;

            if (view == null) {
                view = inflater.inflate(resource, parent, false);
                holder = new SearchResultViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (SearchResultViewHolder) view.getTag();
            }

            holder.onBind(position);

            return view;
        }


        class SearchResultViewHolder {

            View view;
            Text text;

            SearchResultViewHolder(View view) {
                this.view = view;
                text = (Text) view.findViewById(R.id.serialname);
            }

            public void onBind(final int position) {
                text.setText(names.get(position));
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        cancelStartupTask();

                        Intent intent = new Intent(EpisodeListActivity.this, SerialPageActivity.class);
                        intent.putExtra(getString(R.string.media_page_serial_intent), ids.get(position));
                        startActivity(intent);
                    }
                });
            }

        }
    }

    class InitTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected void onPreExecute() {
            if (data.equals("")) showProgressBar();
        }

        @Override
        protected String[] doInBackground(Void[] params) {
            String sStaticData = Web.disableSSLCertificateChecking(Web.url.domain + Web.url.startupData, true);
//            String sStaticData = Web.GET(Web.url.domain + Web.url.startupData, true);

            String data = Web.disableSSLCertificateChecking(Web.url.domain + Web.url.episodes, true);
//            String data = Web.GET(Web.url.domain + Web.url.episodes, true);

            return new String[]{data, sStaticData};
        }

        @Override
        protected void onPostExecute(String[] aVoid) {

            if (!aVoid[0].equals(data)) {
                if (!aVoid[1].equals(staticData)) {
                    PreferenceManager
                            .getDefaultSharedPreferences(EpisodeListActivity.this)
                            .edit()
                            .putString(getString(R.string.main_static_entities), aVoid[1])
                            .apply();
                    initStaticEntities(aVoid[1]);
                }

                PreferenceManager
                        .getDefaultSharedPreferences(EpisodeListActivity.this)
                        .edit()
                        .putString(getString(R.string.main_episode_list), aVoid[0])
                        .apply();
                makeEpisodesList(aVoid[0]);
                invalidateRecycler();
            }

            hideProgressBar();
        }
    }

    private void cancelStartupTask() {
        startupTask.cancel(true);
    }
    private void initStaticEntities(String data) {
        try {
            JSONObject staticData = new JSONObject(data);

            Lang.init(staticData);
            QualityGroup.init(staticData);
            Quality.init(staticData);
            Source.init(staticData);
            Country.init(staticData);
            Genre.init(staticData);
            Network.init(staticData);
            com.iam.oneom.core.entities.Status.init(staticData);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void makeEpisodesList(String data) {
        try {
            JSONObject resp = new JSONObject(data);
            JSONArray eps = resp.getJSONArray("eps");
            int len = eps.length();
            episodes = new ArrayList<>();
            for (int i = 0; i < len; i++) {
                episodes.add(new Episode(eps.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showPopup(final LinkedHashMap<String, String> serials) {
        if (serials != null && serials.size() > 0) {

            popupWindow = new ListPopupWindow(EpisodeListActivity.this);

            final ArrayList<String> names = new ArrayList<>(serials.size());
            ArrayList<String> ids = new ArrayList<>(serials.size());
            for (Map.Entry<String, String> entry : serials.entrySet()) {
                names.add(entry.getKey());
                ids.add(entry.getValue());
            }

            PopupAdapter adapter = new PopupAdapter(EpisodeListActivity.this, R.layout.popup_item, names, ids);

            popupWindow.setAdapter(adapter);
            popupWindow.setModal(false);
            popupWindow.setWidth((int) (Decorator.getScreenWidth() * 0.9));
            popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setAnchorView(searchET);
            popupWindow.show();
        }

        requestEditTextFocus();
    }

    public void requestEditTextFocus() {
        searchET.post(new Runnable() {
            public void run() {
                searchET.requestFocusFromTouch();
                InputMethodManager lManager = (InputMethodManager) EpisodeListActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                lManager.showSoftInput(searchET, 0);
            }
        });
    }

    private void invalidateRecycler() {
        recyclerLayoutManager = new GridLayoutManager(this, 3);
        episodesGrid.setLayoutManager(recyclerLayoutManager);
        adapter = new EpisodesAdapter(episodes, this);
        episodesGrid.addOnScrollListener(episodesRecyclerViewOnScrollListener());

//        recyclerLayoutManager.setSpanSizeLookup(new android.support.v7.widget.GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return adapter.isHeader(position) ? recyclerLayoutManager.getSpanCount() : 1;
//            }
//        });
        episodesGrid.setAdapter(adapter);
    }

    private RecyclerView.OnScrollListener episodesRecyclerViewOnScrollListener() {
        return new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {

                if (dy > 0) {
                    searchView.animate()
                            .setDuration(500)
                            .setStartDelay(0)
                            .translationY(-searchView.getHeight())
                            .setListener(searchAnimatorListener(recyclerView));
                } else if (dy < 0) {
                    searchView.animate()
                            .setDuration(500)
                            .setStartDelay(0)
                            .translationY(0)
                            .setListener(searchAnimatorListener(recyclerView));
                }
            }
        };
    }

    private Animator.AnimatorListener searchAnimatorListener(final RecyclerView recyclerView) {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                recyclerView.clearOnScrollListeners();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                recyclerView.addOnScrollListener(episodesRecyclerViewOnScrollListener());
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
    }
    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }
    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }
}

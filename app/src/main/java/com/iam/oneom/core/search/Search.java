package com.iam.oneom.core.search;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.iam.oneom.core.entities.Entity;
import com.iam.oneom.core.entities.Episode;
import com.iam.oneom.core.entities.Lang;
import com.iam.oneom.core.entities.QualityGroup;
import com.iam.oneom.core.entities.Source;
import com.iam.oneom.core.search.onlines.VodlockerSearchResult;
import com.iam.oneom.core.search.subtitles.OpenSubtitlesSearchResult;
import com.iam.oneom.core.search.torrents.EZTVSearchResult;
import com.iam.oneom.core.search.torrents.ExtraTorrentsSearchResult;
import com.iam.oneom.core.search.torrents.KickAssTorrentSearchResult;
import com.iam.oneom.core.search.torrents.PirateBaySearchResult;
import com.iam.oneom.core.search.torrents.RARBGSearchResult;
import com.iam.oneom.core.util.Web;
import com.iam.oneom.env.widget.CircleProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashMap;

public class Search {

    private static final int EPISODE = 0;
    private static final int LANG = 1;
    private static final int QUALITY_GROUP = 2;

    private static volatile Search instance;

    private Source.Origin origin;
    private HashMap<String, ArrayList<HashMap<Key, String>>> result = new HashMap<>();
    private int page;
    private OnSearchListener listener;
    private CircleProgressBar progressBar;
    private ArrayList<Entity> data;

    public static Search engine(CircleProgressBar progressBar, OnSearchListener listener, Entity... data) {
        Search localInstance = instance;
        if (localInstance == null) {
            synchronized (Search.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Search();
                }
            }
        }
        localInstance.progressBar = progressBar;
        localInstance.listener = listener;
        localInstance.data = new ArrayList<>();
        for (Entity e : data) {
            localInstance.data.add(e);
        }
        return localInstance;
    }

    public static Search instance() {
        Search localInstance = instance;
        if (localInstance == null) {
            synchronized (Search.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Search();
                }
            }
        }
        return localInstance;
    }

    public void clearResults() {
        result = new HashMap<>();
    }

    private void parse(String html, String searchString) {
        if (html != null) {
            Document doc = Jsoup.parse(html);
            SearchResult searchResult = new SearchResult(doc);
            ArrayList<HashMap<Key, String>> result;
            switch (origin) {
                case piratebay:
                    searchResult = new PirateBaySearchResult(doc);
                    break;
                case extratorrent:
                    searchResult = new ExtraTorrentsSearchResult(doc);
                    break;
                case eztv:
                    searchResult = new EZTVSearchResult(doc);
                    break;
                case kickasstorrents:
                    searchResult = new KickAssTorrentSearchResult(doc);
                    break;
                case rarbg:
                    searchResult = new RARBGSearchResult(doc);
                    break;
                case vodlocker:
                    searchResult = new VodlockerSearchResult(doc, searchString);
                    break;
                case opensubtitles:
                    searchResult = new OpenSubtitlesSearchResult(doc,(Lang)data.get(LANG));
                    break;
            }

            result = searchResult.parse();

            if (this.result.get(searchString) == null) {
                this.result.put(searchString, result);
            } else {
                this.result.get(searchString).addAll(result);
            }

            listener.onSearchResult();
        } else {
            Toast.makeText(progressBar.getContext(), "нет результатов", Toast.LENGTH_SHORT).show();
        }
    }

    public void find(Source source, String searchString) {

        Source.Origin origin = Source.Origin.valueOf(source.name().toLowerCase());
        if (this.origin == null || origin != this.origin) {
            result = new HashMap<>();
            page = 0;
            this.origin = origin;
        }

        SearchTask task = new SearchTask();
        task.execute();
    }

    public ArrayList<HashMap<Key, String>> results(String searchString, Source.Origin origin) {

        if (origin == this.origin && result.get(searchString) != null) {
            return result.get(searchString);
        } else {
            return new ArrayList<>();
        }

//        return result.get(searchString) == null ? new ArrayList<HashMap<Key, String>>() : result.get(searchString);
    }

    private class SearchTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {

            ArrayList<String> search_params = new ArrayList<>();
            String searchString;

            switch (origin) {
                case opensubtitles:
                    search_params.add(lang().shortName());
                    search_params.add(lang().openSubtitlesShort() + "");
                    search_params.add(episode().season() + "");
                    search_params.add(episode().ep() + "");

                    searchString = episode().serial().title();
                    search_params.add(searchString);
                    return new String[]{
                            Web.search(origin, search_params, false), searchString
                    };
                default:
                    searchString = episode().serial().title();
                    search_params.add(searchString);
                    search_params.add((page++) + "");
                    return new String[]{
                            Web.search(origin, search_params), searchString
                    };
            }


        }

        @Override
        protected void onPostExecute(String[] s) {
            progressBar.setVisibility(View.INVISIBLE);
            parse(s[0], s[1]);
        }
    }

    private Lang lang() {
        if (data.get(LANG) instanceof Lang)
            return ((Lang)data.get(LANG));

        throw new RuntimeException("You must pass Lang as fourth parameter of engine(progress,listener,entities...)");
    }

    private Episode episode() {
        if (data.get(EPISODE) instanceof Episode)
            return ((Episode) data.get(EPISODE));

        throw new RuntimeException("You must pass Episode as third parameter of engine(progress,listener,entities...)");
    }

    private QualityGroup qualityGroup() {
        if (data.get(QUALITY_GROUP) instanceof QualityGroup)
            return ((QualityGroup) data.get(QUALITY_GROUP));

        throw new RuntimeException("You must pass QualityGroup as fifth parameter of engine(progress,listener,entities...)");
    }

    public interface OnSearchListener {
        void onSearchResult();
    }
}

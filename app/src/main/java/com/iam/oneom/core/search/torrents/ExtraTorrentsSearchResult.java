package com.iam.oneom.core.search.torrents;

import com.iam.oneom.core.search.Key;
import com.iam.oneom.core.search.SearchResult;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

public class ExtraTorrentsSearchResult extends SearchResult {

    public ExtraTorrentsSearchResult(Document document) {
        super(document);
    }

    @Override
    public ArrayList<HashMap<Key, String>> parse() {

        ArrayList<HashMap<Key, String>> result = new ArrayList<>();

        Element extraTorrentTable = document.select("table").get(14);
        Elements extraTorrentsSearchResults = extraTorrentTable.select("tbody").get(1).select("tr");
        for (int i = 0; i < extraTorrentsSearchResults.size(); i++) { //first row is the col names so skip it.
            Element extraTorrentsTableRow = extraTorrentsSearchResults.get(i);

            Elements etCols = extraTorrentsTableRow.select("td");


            String title = etCols.get(2).select("a").attr("title").replace("view ", "").replace(" torrent", "");
            if (title.toLowerCase().equals("view comments")) {
                title = etCols.get(2).select("a").get(1).attr("title").replace("view ", "").replace(" torrent", "");
            }

            HashMap<Key, String> parseTuple = new HashMap<>();

            parseTuple.put(Key.Name, title);
            parseTuple.put(Key.Page, "http://extratorrent.cc" + etCols.get(2).select("a").attr("href"));
            parseTuple.put(Key.Download, "http://extratorrent.cc" + etCols.get(0).select("a").attr("href"));
            parseTuple.put(Key.Size, etCols.get(3).text());
            parseTuple.put(Key.Seeds, etCols.get(4).text());
            parseTuple.put(Key.Leachs, etCols.get(5).text());

            result.add(parseTuple);
        }
        return result;
    }
}

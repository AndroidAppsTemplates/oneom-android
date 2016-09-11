package com.iam.oneom.core.search.torrents;

import com.iam.oneom.core.search.Key;
import com.iam.oneom.core.search.SearchResult;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

public class RARBGSearchResult extends SearchResult {

    public RARBGSearchResult(Document document) {
        super(document);
    }

    @Override
    public ArrayList<HashMap<Key, String>> parse() {

        ArrayList<HashMap<Key, String>> result = new ArrayList<>();

        Element rarbgTable = document.select("table").get(11);
        Elements rarbgSearchResults = rarbgTable.select("tbody").select("tr");
        for (int i = 1; i < rarbgSearchResults.size(); i++) { //first row is the col names so skip it.
            Element rarbgTableRow = rarbgSearchResults.get(i);
            Elements rarbgCol = rarbgTableRow.select("td");
            HashMap<Key, String> parseTuple = new HashMap<>();
            parseTuple.put(Key.Name, rarbgCol.get(1).select("a").attr("title"));
            parseTuple.put(Key.Page, "https://rarbg.to" + rarbgCol.get(1).select("a").attr("href"));
            parseTuple.put(Key.Download, "https://rarbg.to" + rarbgCol.get(1).select("a").get(1).attr("href"));
            parseTuple.put(Key.Size, rarbgCol.get(3).text());
            parseTuple.put(Key.Seeds, rarbgCol.get(4).text());
            parseTuple.put(Key.Leachs, rarbgCol.get(5).text());

            result.add(parseTuple);
        }

        return result;
    }
}

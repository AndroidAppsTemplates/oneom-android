package com.iam.oneom.core.search.torrents;

import com.iam.oneom.core.search.Key;
import com.iam.oneom.core.search.SearchResult;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

public class EZTVSearchResult extends SearchResult {


    public EZTVSearchResult(Document document) {
        super(document);
    }

    @Override
    public ArrayList<HashMap<Key, String>> parse() {

        ArrayList<HashMap<Key, String>> result = new ArrayList<>();

        Element eztvTable = document.select("table").get(2);
        Elements eztvSearchResults = eztvTable.select("tbody").select("tr");
        for (int i = 2; i < eztvSearchResults.size(); i++) { //first row is the col names so skip it.
            Element eztvTableRow = eztvSearchResults.get(i);

            Elements eztvCols = eztvTableRow.select("td");

            HashMap<Key, String> parseTuple = new HashMap<>();

            parseTuple.put(Key.Name, eztvCols.get(1).select("a[href]").html());
            parseTuple.put(Key.Page, "https://eztv.ag" + eztvCols.get(1).select("a").get(0).attr("href"));
            parseTuple.put(Key.Download, "https://eztv.ag" + eztvCols.get(2).select("a").attr("href"));
            parseTuple.put(Key.Size, eztvCols.get(3).text());

            result.add(parseTuple);
        }

        return result;
    }
}

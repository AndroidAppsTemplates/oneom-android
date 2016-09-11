package com.iam.oneom.core.search.onlines;

import com.iam.oneom.core.search.Key;
import com.iam.oneom.core.search.SearchResult;
import com.iam.oneom.core.util.Editor;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

public class VodlockerSearchResult extends SearchResult {

    private String searchString;

    public VodlockerSearchResult(Document document, String searchString) {
        super(document);
        this.searchString = searchString;
    }

    @Override
    public ArrayList<HashMap<Key, String>> parse() {

        ArrayList<HashMap<Key, String>> result = new ArrayList<>();

        Elements table = document.select("table");
        Elements rows = table.select("tr");

        for (int i = 1; i < rows.size(); i++) {
            Element row = rows.get(i);

            Elements cols = row.select("td");
            Element[] columns = new Element[2];
            for (int j = 0; j < cols.size(); j++) {
                columns[j] = cols.get(j);
            }

            HashMap<Key, String> parseTuple = new HashMap<>();

            String post = columns[0].select("a[href]").attr("style");
            parseTuple.put(Key.PosterUrl, Editor.posterLink(post));
            String title = columns[1].select("div.link").select("a[href]").text();
            parseTuple.put(Key.Name, title.equals("") ? searchString : title);
            parseTuple.put(Key.Page, columns[1].select("div.link").select("a[href]").attr("href"));

            result.add(parseTuple);
        }

        return result;
    }
}

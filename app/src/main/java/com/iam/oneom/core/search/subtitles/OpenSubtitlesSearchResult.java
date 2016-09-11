package com.iam.oneom.core.search.subtitles;

import com.iam.oneom.core.entities.Lang;
import com.iam.oneom.core.search.Key;
import com.iam.oneom.core.search.SearchResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

public class OpenSubtitlesSearchResult extends SearchResult {

    private Lang lang;

    public OpenSubtitlesSearchResult(Document document, Lang lang) {
        super(document);
        this.lang = lang;
    }

    @Override
    public ArrayList<HashMap<Key, String>> parse() {
        ArrayList<HashMap<Key, String>> result = new ArrayList<>();

        Elements ostable = document.select("tbody > tr[id~=name[0-9]{7}]");

        for (int i = 1; i < ostable.size(); i += 1) {
            Element row = ostable.get(i);

            HashMap<Key, String> parseTuple = new HashMap<>();

            parseTuple.put(Key.Page, "http://www.opensubtitles.org" + row.attr("onclick").split(",")[1].replaceAll("'", ""));

            Elements td = row.select("td[id~=main[0-9]{7}]");
            String id = td.attr("id").replace("main", "");
            String name = td.toString().split("<br>")[1];

            if (name.contains("span")) {
                String prefix = name.split("<")[0];
                Document document = Jsoup.parse(name);
                name = prefix + document.select("span").attr("title");
            }

            String time = row.select("time").attr("datetime");



            //

            parseTuple.put(Key.Name, name);
            parseTuple.put(Key.Download, "http://dl.opensubtitles.org/en/download/sub/vrf-" + id);
            parseTuple.put(Key.Uploaded, time);
            parseTuple.put(Key.Language, (lang.name()));

            result.add(parseTuple);
        }

        return result;
    }
}

package com.iam.oneom.core.search.torrents;

import com.iam.oneom.core.search.Key;
import com.iam.oneom.core.search.SearchResult;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

public class KickAssTorrentSearchResult extends SearchResult {

    public KickAssTorrentSearchResult(Document document) {
        super(document);
    }

    @Override
    public ArrayList<HashMap<Key, String>> parse() {

        ArrayList<HashMap<Key, String>> result = new ArrayList<>();

        Element kickAssTorrentsTable = document.select("table").get(1);
        Elements kickAssTorrentsSearchResults = kickAssTorrentsTable.select("tbody").select("tr");
        for (int i = 1; i < kickAssTorrentsSearchResults.size(); i++) { //first row is the col names so skip it.
            Element kickAssTorrentsTableRow = kickAssTorrentsSearchResults.get(i);
            Element katCol = kickAssTorrentsTableRow.select("td").get(0);
            HashMap<Key, String> parseTuple = new HashMap<>();
            parseTuple.put(Key.Page, "https://kat.cr" + katCol.select("a").get(1).attr("href"));
            parseTuple.put(Key.Download, katCol.select("a").get(2).attr("href"));
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(katCol.select("div").get(1).attr("data-sc-params"));
                parseTuple.put(Key.Name, URLDecoder.decode(jsonObject.getString("name"), "UTF-8"));
            } catch (JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            parseTuple.put(Key.Seeds, kickAssTorrentsTableRow.select("td").get(4).text());
            parseTuple.put(Key.Leachs, kickAssTorrentsTableRow.select("td").get(5).text());
            parseTuple.put(Key.Size, kickAssTorrentsTableRow.select("td").get(1).text());

            result.add(parseTuple);
        }

        return result;
    }
}

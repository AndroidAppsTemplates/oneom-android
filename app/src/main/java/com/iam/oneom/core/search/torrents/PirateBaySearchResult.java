package com.iam.oneom.core.search.torrents;


import com.iam.oneom.core.search.Key;
import com.iam.oneom.core.search.SearchResult;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

public class PirateBaySearchResult extends SearchResult {

    public PirateBaySearchResult(Document document) {
        super(document);
    }

    @Override
    public ArrayList<HashMap<Key, String>> parse() {
        ArrayList<HashMap<Key, String>> result = new ArrayList<>();
        System.out.println("doc " + document);
        Elements pirateBayTable = document.select("table");//.select("tbody");
        System.out.println(pirateBayTable.toString() + "\n\n");
        Elements pirateBayTableRows = pirateBayTable.select("tr");
        System.out.println("rows count " + pirateBayTableRows.size());
        for (int i = 1; i < pirateBayTableRows.size(); i++) { //first row is the col names so skip it.

            Element pirateBayTableRow = pirateBayTableRows.get(i);

            Elements pbCols = pirateBayTableRow.select("td");
            Element pbCol = pbCols.get(1);


            HashMap<Key, String> parseTuple = new HashMap<>();


            parseTuple.put(Key.Name, pbCol.select("a").get(0).html());
            parseTuple.put(Key.Download, pbCol.select("a").get(1).attr("href"));
            parseTuple.put(Key.Page, "https://thepiratebay.cr" + pbCol.select("a").get(0).attr("href"));
            parseTuple.put(Key.Seeds, pbCols.get(2).text());
            parseTuple.put(Key.Leachs, pbCols.get(3).text());

            String[] descr = pbCol.select("font").text().split(" ");

            parseTuple.put(Key.Size, descr[3].replace(",", ""));



            result.add(parseTuple);
        }
        return result;
    }

}

package com.iam.oneom.pages.main.search.online.naya;

import com.iam.oneom.pages.main.search.online.OnlineParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iam on 27.05.17.
 */

public class NayaParser extends OnlineParser<NayaSearchResult> {
    @Override
    public List<NayaSearchResult> parse(String s) {
        Document document = Jsoup.parse(s);
        Elements elements = document.select("div.item-video");

        ArrayList<NayaSearchResult> results = new ArrayList<>();

        for (Element e : elements) {
            NayaSearchResult result = new NayaSearchResult();
            result.parse(e.toString());
            results.add(result);
        }

        return results;
    }
}

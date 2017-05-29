package com.iam.oneom.pages.main.search.online.vodlocker;

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

public class VodlokerParser extends OnlineParser<VodlockerSearchResult> {
    @Override
    public List<VodlockerSearchResult> parse(String s) {
        Document document = Jsoup.parse(s);
        Elements elements = document.body().getElementsByClass("vid_block");

        ArrayList<VodlockerSearchResult> results = new ArrayList<>();

        for (Element e : elements) {
            VodlockerSearchResult result = new VodlockerSearchResult();
            result.parse(e.toString());
            results.add(result);
        }

        return results;
    }
}

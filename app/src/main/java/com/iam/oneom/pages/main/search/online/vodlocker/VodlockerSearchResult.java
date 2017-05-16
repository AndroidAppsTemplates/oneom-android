package com.iam.oneom.pages.main.search.online.vodlocker;

import com.iam.oneom.pages.main.search.online.OnlineSearchResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by iam on 13.05.17.
 */

public class VodlockerSearchResult extends OnlineSearchResult {

    @Override
    public void parse(String html) {
        Document document = Jsoup.parse(html);
        setName(document.getElementsByClass("link").text());
        setUrl(document.getElementsByClass("link").attr("href"));
        setPosterUrl(document.getElementsByTag("img").attr("src"));
    }

}

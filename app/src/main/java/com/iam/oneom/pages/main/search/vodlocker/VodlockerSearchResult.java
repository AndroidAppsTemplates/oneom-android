package com.iam.oneom.pages.main.search.vodlocker;

import com.iam.oneom.pages.main.search.OnlineSearchResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by iam on 13.05.17.
 */

public class VodlockerSearchResult extends OnlineSearchResult {

    @Override
    public void parse(String html) {
        Document document = Jsoup.parse(html);
    }

}

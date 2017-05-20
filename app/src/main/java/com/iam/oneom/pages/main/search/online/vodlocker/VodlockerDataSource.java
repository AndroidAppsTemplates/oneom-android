package com.iam.oneom.pages.main.search.online.vodlocker;

import com.iam.oneom.core.CustomRequest;
import com.iam.oneom.pages.main.search.SearchDataSource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iam on 13.05.17.
 */

public class VodlockerDataSource extends SearchDataSource<VodlockerSearchResult> {

    public VodlockerDataSource(SearchDataSource.OnSearchListener<VodlockerSearchResult> listener) {
        super(listener);
    }

    @Override
    public void search(String searchString, String searchForm) {

        CustomRequest.instance.request(searchForm.replace("{searchString}", searchString).replace("{page}", "1"))
                .subscribe(s -> {
                    if (listener != null) {
                        listener.getResults(parse(s));
                    }
                });
    }

    private List<VodlockerSearchResult> parse(String s) {
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

    @Override
    public void searchAtPage(int page) {

    }
}

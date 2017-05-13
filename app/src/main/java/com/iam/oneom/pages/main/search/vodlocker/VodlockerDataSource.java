package com.iam.oneom.pages.main.search.vodlocker;

import com.iam.oneom.core.CustomRequest;
import com.iam.oneom.pages.main.search.SearchDataSource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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

        CustomRequest.instance.request(searchForm.replace("{searchString}", searchString).replace(".com", ".li"))
                .subscribe(s -> {
                    if (listener != null) {
                        listener.getResults(parse(s));
                    }
                });
    }

    private List<VodlockerSearchResult> parse(String s) {
        Document document = Jsoup.parse(s);

        return new ArrayList<>();
    }

    @Override
    public void searchAtPage(int page) {

    }
}

package com.iam.oneom.core.search;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchResult {

    protected Document document;

    public SearchResult(Document document) {
        this.document = document;
    }

    public ArrayList<HashMap<Key, String>> parse() {
        return new ArrayList<>();
    }

}

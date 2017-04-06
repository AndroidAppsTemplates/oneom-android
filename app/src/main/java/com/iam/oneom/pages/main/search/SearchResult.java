package com.iam.oneom.pages.main.search;

/**
 * Created by iam on 07.04.17.
 */

public interface SearchResult {
    String getName();
    String getDownloadUrl();
    void parse(String html);
}

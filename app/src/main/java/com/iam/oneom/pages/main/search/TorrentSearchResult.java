package com.iam.oneom.pages.main.search;

/**
 * Created by iam on 07.04.17.
 */

public interface TorrentSearchResult extends SearchResult {
    int getSeeds();
    int getLeeches();
    long getSize();
}

package com.iam.oneom.pages.main.search.online;

import com.iam.oneom.pages.main.search.SearchResult;

/**
 * Created by iam on 07.04.17.
 */

public abstract class OnlineSearchResult implements SearchResult {


    private String name;
    private String url;
    private String posterUrl;

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDownloadUrl() {
        return url;
    }

    public String getPosterUrl() {
        return posterUrl;
    }
}

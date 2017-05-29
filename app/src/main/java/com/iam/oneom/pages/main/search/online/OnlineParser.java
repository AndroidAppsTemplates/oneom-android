package com.iam.oneom.pages.main.search.online;

import java.util.List;

/**
 * Created by iam on 27.05.17.
 */

public abstract class OnlineParser<T extends OnlineSearchResult> {
    public abstract List<T> parse(String s);
}

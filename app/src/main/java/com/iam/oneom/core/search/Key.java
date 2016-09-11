package com.iam.oneom.core.search;

public enum Key {

    Name("name"),
    Download("download"),

    /**
     * TORRENT KEYS
     */
    Seeds("seeds"),
    Leachs("leachs"),
    Size("size"),
    Page("page"),

    /**
     * ONLINE KEYS
     */
    PosterUrl("posterurl"),
    VideoLink("videolink"),

    /**
     * SUBTITLE KEYS
     */
    Uploaded("uploaded"),
    Language("language");

    Key(String key) {
        this.key = key;
    }

    String key;
}
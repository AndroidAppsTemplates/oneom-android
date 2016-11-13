package com.iam.oneom.core.entities.model;

import com.iam.oneom.core.entities.interfaces.Named;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Source extends RealmObject implements Named {

    @PrimaryKey
    private int id;
    private int typeId; // TODO implement into deserializer
    private String searchPage;
    private String search;
    private String searchStep;
    private String name;
    private String data;
    private String login;
    private String url;
    private Origin origin;
    private Type type;

    public enum Type {
        Torrent(1),
        Online(3),
        Subtitle(2);

        Type(int type) {
            this.type = type;
        }

        private int type;
    }

    @Override
    public String name() {
        return name;
    }

    public enum Origin {
        vodlocker,
        piratebay,
        extratorrent,
        kickasstorrents,
        opensubtitles,
        rarbg,
        eztv,
        // TODO make a handler
        torrentdownloads,
        tracker1337x,
        rutor,
        rutracker,
        bitsnoop,
        torrentz;
    }
}

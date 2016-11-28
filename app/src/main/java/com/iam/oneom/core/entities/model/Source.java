package com.iam.oneom.core.entities.model;

import com.iam.oneom.core.entities.interfaces.Named;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Source extends RealmObject implements Named {

    @PrimaryKey
    private long id;
    private int typeId; // TODO implement into deserializer
    private int active;
    private String searchPage;
    private String search;
    private String searchStep;
    private String name;
    private String data;
    private int login;
    private String url;
//    private Origin origin;
//    private Type type;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setType(int typeId) {
        this.typeId = typeId;
//        this.type = Type.byId(typeId);
    }

    public String getSearchPage() {
        return searchPage;
    }

    public void setSearchPage(String searchPage) {
        this.searchPage = searchPage;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getSearchStep() {
        return searchStep;
    }

    public void setSearchStep(String searchStep) {
        this.searchStep = searchStep;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getLogin() {
        return login;
    }

    public void setLogin(int login) {
        this.login = login;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

//    public enum Type {
//        Torrent(1),
//        Online(3),
//        Subtitle(2);
//
//        Type(int type) {
//            this.type = type;
//        }
//
//        @Nullable
//        private static Type byId(int type_id) {
//            for (Type type : values()) {
//                if (type.type == type_id) {
//                    return type;
//                }
//            }
//            try {
//                throw new RuntimeException("Unknown source type " + type_id);
//            } catch (Exception e) {
//                ErrorHandler.handleError(Thread.currentThread(), e);
//            }
//            return null;
//        }
//
//        private int type;
//    }
//
//    public enum Origin {
//        vodlocker,
//        piratebay,
//        extratorrent,
//        kickasstorrents,
//        opensubtitles,
//        rarbg,
//        eztv,
//        // TODO make a handler
//        torrentdownloads,
//        tracker1337x,
//        rutor,
//        rutracker,
//        bitsnoop,
//        torrentz;
//    }
}

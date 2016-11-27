package com.iam.oneom.core.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.iam.oneom.core.entities.interfaces.Named;
import com.iam.oneom.pages.OneOm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Source extends  Entity implements Parcelable, Named {

    private static ArrayList<Source> sources = new ArrayList<>();
    private static ArrayList<Source> torrents = new ArrayList<>();
    private static ArrayList<Source> onlines = new ArrayList<>();
    private static ArrayList<Source> subtitles = new ArrayList<>();

    private int typeId;
    private String searchPage;
    private String search;
    private String searchStep;
    private String name;
    private String data;
    private String login;
    private String url;
    private Origin origin;

    public Source(JSONObject json){
        super(json);
        try {
            name = json.getString("name");
            origin = Origin.valueOf(name.toLowerCase());
            typeId = json.getInt("type_id");
            searchPage = json.getString("search_page");
            search = json.getString("search");
            searchStep = json.getString("search_step");
            data = json.getString("data");
            login = json.getString("login");
            url = json.getString("url");

            if (origin == Origin.piratebay) {
                searchPage = searchPage.replaceFirst("http", "https");
                search = search.replaceFirst("http", "https");
            }
        } catch (JSONException e) {
//            OneOm.handleError(Thread.currentThread(), e, "Source main constructor");
            e.printStackTrace();
        }
    }


    protected Source(Parcel in) {
        super(in);
        typeId = in.readInt();
        searchPage = in.readString();
        search = in.readString();
        searchStep = in.readString();
        name = in.readString();
        data = in.readString();
        login = in.readString();
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(typeId);
        dest.writeString(searchPage);
        dest.writeString(search);
        dest.writeString(searchStep);
        dest.writeString(name);
        dest.writeString(data);
        dest.writeString(login);
        dest.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Source> CREATOR = new Creator<Source>() {
        @Override
        public Source createFromParcel(Parcel in) {
            return new Source(in);
        }

        @Override
        public Source[] newArray(int size) {
            return new Source[size];
        }
    };

    public String searchPage() { return searchPage; }
    public String search() { return search; }
    public String searchStep() { return searchStep; }

    @Override
    public String getName() {
        return name;
    }

    public String data() { return data; }
    public String login() { return login; }
    public String url() { return url; }
    public Origin origin() { return origin; }

    private static void add(Source source) {
        if (!sources.contains(source)) {
            sources.add(source);
            switch (source.typeId) {
                case 1:
                    torrents.add(source);
                    break;
                case 2:
                    subtitles.add(source);
                    break;
                case 3:
                    onlines.add(source);
                    break;
            }
        }
    }

    public static boolean contains(Source source) {
        return sources.contains(source);
    }

    public static Source getBySource(Source source) {
        for (Source s : sources) {
            if (s.equals(source)) return s;
        }
        throw new RuntimeException("This sourse doesnt contains in source list");
    }

    public static Source getByOrigin(Origin origin) {
        for (Source s : sources) {
            if (s.origin() == origin) return s;
        }
        throw new RuntimeException("source unknown");
    }

    public static Source getByID(String id) {
        for (Source source : sources) {
            if (source.id() == Integer.parseInt(id)) {
                return source;
            }
        }
        throw new RuntimeException("Source with id " + id + " doesnt contains in source list");
    }

    public static Source getByType(Type type, int position) {
        ArrayList<Source> sources = new ArrayList<>(0);
        switch (type) {
            case Torrent:
                sources = Source.torrents;
                break;
            case Subtitle:
                sources = Source.subtitles;
                break;
            case Online:
                sources = Source.onlines;
                break;
        }

        if (position < sources.size() && position >= 0) {
            return sources.get(position);
        } else {
            throw new IndexOutOfBoundsException
                    (
                            "public static Source getByType(Type type, int position)" +
                                    type.name() + " size is " + sources.size() +
                                    " requested index is " + position
                    );
        }
    }

    public static void init(JSONObject data) throws JSONException {
        JSONArray sources = data.getJSONArray("source");
        int lSources = sources.length();
        for (int i = 0; i < lSources; i++) {
            Source.add(new Source(sources.getJSONObject(i)));
        }
    }

    public static ArrayList<String> names(Type type) {
        ArrayList<String> names = new ArrayList<>();
        int t = 0;
        switch (type) {
            case Torrent:
                t = 1;
                break;
            case Online:
                t = 3;
                break;
            case Subtitle:
                t = 2;
                break;
        }
        for (Source s : sources) {
            if (s.typeId == t) {
                names.add(s.getName());
            }
        }
        return names;
    }

    public static ArrayList<Source> list() {
        return sources;
    }

    public enum Type {
        Torrent,
        Online,
        Subtitle;
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
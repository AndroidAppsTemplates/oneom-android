package com.iam.oneom.core.entities.old;

import android.os.Parcel;
import android.os.Parcelable;

import com.iam.oneom.core.util.Editor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

public class Serial extends Entity implements Parcelable {

    private static HashSet<Serial> SERIALS = new HashSet<>();

    private Poster poster;
    private ArrayList<Network> networks;
    private ArrayList<Genre> genres;
    private Status status;
    private ArrayList<Country> countries;
    private ArrayList<Episode> episodes;

    private int seasonsCount = 0;

    private String title;
    private boolean hide;
    private String tvrageId;
    private String tvmazeId;
    private String mdbId;
    private String tvdbId;
    private String vkGroupId;
    private String imdbId;
    private String imdbRating;
//    private String statusId;
//    private String posterId;
    private int runtime;
    private String start;
    private String end;
    private String airtime;
    private String airdate;
    private String timezone;

    public Serial(JSONObject data)  {
        super(data);
        try {
            this.title = data.getString("title");
            this.hide = data.getString("hide").equals("1");
            this.tvrageId = data.getString("tvrage_id");
            this.tvmazeId = data.getString("tvmaze_id");
            this.mdbId = data.getString("mdb_id");
            this.tvdbId = data.getString("tvdb_id");
            this.vkGroupId = data.getString("vk_group_id");
            this.imdbId = data.getString("imdb_id");
            this.imdbRating = data.getString("imdb_rating");
//            this.statusId = data.getString("status_id");
//            this.posterId = data.getString("poster_id");
            final String runtime = data.getString("runtime");
            this.runtime = runtime.equals("null") ? 0 : Integer.parseInt(runtime);
            this.start = data.getString("start");
            this.end = data.getString("end");
            this.airtime = data.getString("airtime");
            if (data.has("airdate")) this.airdate = data.getString("airdate");
            this.timezone = data.getString("timezone");
            if (!data.getString("poster").equals("null"))
                this.poster = new Poster(data.getJSONObject("poster"));

            networks = new ArrayList<>();
            if (data.has("network")) {
                JSONArray jNetworks = data.getJSONArray("network");
                for (int i = 0, l = jNetworks.length(); i < l; i++) {
                    networks.add(Network.getByID(jNetworks.getJSONObject(i).getString("id")));
                }
            }

            genres = new ArrayList<>();
            if (data.has("genre")) {
                JSONArray jGenres = data.getJSONArray("genre");
                for (int i = 0, l = jGenres.length(); i < l; i++) {
                    genres.add(Genre.getByID(jGenres.getJSONObject(i).getString("id")));
                }
            }

            countries = new ArrayList<>();
            if (data.has("country")) {
                JSONArray jCountries = data.getJSONArray("country");
                for (int i = 0, l = jCountries.length(); i < l; i++) {
                    countries.add(Country.getByID(jCountries.getJSONObject(i).getString("id")));
                }
            }

            if (data.has("status_id")) status = Status.getByID(data.getString("status_id"));
            else status = Status.getByID(Status.UNKNOWN_STATUS_ID);


            episodes = new ArrayList<>();
            if (data.has("ep")) {
                JSONArray jEpisodes = data.getJSONArray("ep");
                for (int i = 0, l = jEpisodes.length(); i < l; i++) {
                    Episode ep = new Episode(jEpisodes.getJSONObject(i));
                    episodes.add(ep);
                    int seasonNumber = ep.season();
                    if (seasonNumber > seasonsCount) seasonsCount = seasonNumber;
                }
            }
        } catch (JSONException e) {
//            OneOm.handleError(Thread.currentThread(), e, "Serial main constructor");
            e.printStackTrace();
        }
    }

    public int seasonsCount() { return seasonsCount; }
    public int episodesCount() { return episodes.size(); }

    public ArrayList<Episode> episodesBySeason(int season) {
        ArrayList<Episode> res = new ArrayList<>();

        for (Episode e : episodes) {
            if (e.season() == season) res.add(e);
        }
        return res;
    }

    protected Serial(Parcel in) {
        super(in);
        poster = in.readParcelable(Poster.class.getClassLoader());
        networks = in.createTypedArrayList(Network.CREATOR);
        genres = in.createTypedArrayList(Genre.CREATOR);
        status = in.readParcelable(Status.class.getClassLoader());
        countries = in.createTypedArrayList(Country.CREATOR);
        episodes = in.createTypedArrayList(Episode.CREATOR);
        seasonsCount = in.readInt();
        title = in.readString();
        hide = in.readByte() != 0;
        tvrageId = in.readString();
        tvmazeId = in.readString();
        mdbId = in.readString();
        tvdbId = in.readString();
        vkGroupId = in.readString();
        imdbId = in.readString();
        imdbRating = in.readString();
        runtime = in.readInt();
        start = in.readString();
        end = in.readString();
        airtime = in.readString();
        airdate = in.readString();
        timezone = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(poster, flags);
        dest.writeTypedList(networks);
        dest.writeTypedList(genres);
        dest.writeParcelable(status, flags);
        dest.writeTypedList(countries);
        dest.writeTypedList(episodes);
        dest.writeInt(seasonsCount);
        dest.writeString(title);
        dest.writeByte((byte) (hide ? 1 : 0));
        dest.writeString(tvrageId);
        dest.writeString(tvmazeId);
        dest.writeString(mdbId);
        dest.writeString(tvdbId);
        dest.writeString(vkGroupId);
        dest.writeString(imdbId);
        dest.writeString(imdbRating);
        dest.writeInt(runtime);
        dest.writeString(start);
        dest.writeString(end);
        dest.writeString(airtime);
        dest.writeString(airdate);
        dest.writeString(timezone);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Serial> CREATOR = new Creator<Serial>() {
        @Override
        public Serial createFromParcel(Parcel in) {
            return new Serial(in);
        }

        @Override
        public Serial[] newArray(int size) {
            return new Serial[size];
        }
    };

    public String imdbRating() { return imdbRating; }
    public String runtime() { return String.valueOf(runtime); }
    public String genres() { return Editor.namesByComma(genres); }
    public String networks() { return Editor.namesByComma(networks); }
    public String countries() { return Editor.namesByComma(countries); }
    public String status() { return status.getName(); }
    public String airstart() { return start == null ? "" : start; }
    public String airend() { return end == null ? "" : end; }
    public String title() { return title; }
    public Poster poster() { return poster; }

    public static void add(Serial serial) {
        String id = serial.id() + "";
        if (Serial.containsId(id)) {
            Serial being = Serial.getByID(id);
            if (serial.episodes.size() > being.episodes.size()) {
                SERIALS.add(serial);
            }
        } else {
            SERIALS.add(serial);
        }
    }

    public static boolean contains(Serial serial) {
        return SERIALS.contains(serial);
    }

    public static boolean containsId(String id) {
        for (Serial s : SERIALS) {
            if (s.id() == Integer.parseInt(id)) return true;
        }
        return false;
    }

    public static Serial getByStatus(Serial serial) {
        for (Serial s : SERIALS) {
            if (s.equals(serial)) return s;
        }
        throw new RuntimeException("This serial doesn't contains in serials list");
    }

    public static Serial getByID(String id) {
        for (Serial s : SERIALS) {
            if (s.id() == Integer.parseInt(id)) {
                return s;
            }
        }
        throw new RuntimeException("Serials with id " + id + " doesn't contains in serials list");
    }

    public String posterURL() {
        if (poster != null) {
            return poster.original();
        } else {
            return "null";
        }
    }

    @Override
    public boolean equals(Object o) {
        return ((Serial)o).id() == this.id();
    }
}

package com.iam.oneom.core.entities.old;

import android.os.Parcel;
import android.os.Parcelable;

import com.iam.oneom.core.entities.interfaces.Named;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Genre extends Entity implements Parcelable, Named {

    private static ArrayList<Genre> GENRES = new ArrayList<>();
    private String name;

    public Genre(JSONObject jGenre) {
        super(jGenre);
        try {
            name = jGenre.getString("name");
        } catch (JSONException e) {
//            OneOm.handleError(Thread.currentThread(), e, "Genre main constructor");
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    private static void add(Genre genre) {
        if (!GENRES.contains(genre)) {
            GENRES.add(genre);
        }
    }

    public static boolean contains(Genre genre) {
        return GENRES.contains(genre);
    }

    public static Genre getByGenre(Genre genre) throws RuntimeException {
        for (Genre g : GENRES) {
            if (g.equals(genre)) return g;
        }
        throw new RuntimeException("This genre doesn't contains in genres list");
    }

    public static Genre getByID(String id) throws RuntimeException {
        for (Genre g : GENRES) {
            if (g.id() == Integer.parseInt(id)) {
                return g;
            }
        }
        throw new RuntimeException("Genre with id " + id + " doesnt contains in genres list");
    }

    public static void init(JSONObject data) throws JSONException {
        JSONArray genres = data.getJSONArray("genre");
        int lGenres = genres.length();
        for (int i = 0; i < lGenres; i++) {
            Genre.add(new Genre(genres.getJSONObject(i)));
        }
    }

    protected Genre(Parcel in) {
        super(in);
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Genre> CREATOR = new Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel in) {
            return new Genre(in);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };
}

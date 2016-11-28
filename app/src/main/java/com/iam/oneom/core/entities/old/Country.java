package com.iam.oneom.core.entities.old;

import android.os.Parcel;
import android.os.Parcelable;

import com.iam.oneom.core.entities.interfaces.Named;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Country extends Entity implements Parcelable, Named {

    private static ArrayList<Country> countries = new ArrayList<>();

    private String name;

    public Country(JSONObject jsonObject) {
        super(jsonObject);
        try {
            name = jsonObject.getString("name");
        } catch (JSONException e) {
//            OneOm.handleError(Thread.currentThread(), e, "Country main constructor");

            e.printStackTrace();
        }
    }

    protected Country(Parcel in) {
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

    public static final Creator<Country> CREATOR = new Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel in) {
            return new Country(in);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };

    private static void add(Country country) {
        if (!countries.contains(country)) {
            countries.add(country);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    public static boolean contains(Country country) {
        return countries.contains(country);
    }

    public static Country getByCountry(Country country) throws RuntimeException {
        for (Country c : countries) {
            if (c.equals(country)) return c;
        }
        throw new RuntimeException("This country doesnt contains in countries list");
    }

    public static Country getByID(String id) throws RuntimeException {
        for (Country c : countries) {
            if (c.id() == Integer.parseInt(id)) {
                return c;
            }
        }
        throw new RuntimeException("Country with id " + id + " doesnt contains in countries list");
    }

    public static void init(JSONObject data) throws JSONException {
        JSONArray countries = data.getJSONArray("country");
        int lCountries = countries.length();
        for (int i = 0; i < lCountries; i++) {
            Country.add(new Country(countries.getJSONObject(i)));
        }
    }
}

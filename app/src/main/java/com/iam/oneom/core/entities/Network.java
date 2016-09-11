package com.iam.oneom.core.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.iam.oneom.core.entities.interfaces.Named;
import com.iam.oneom.pages.OneOm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Network extends Entity implements Parcelable, Named {

    private static ArrayList<Network> NETWORKS = new ArrayList<>();

    private String name;
    private Country country;
    private String countryId;

    public Network(JSONObject jNetwork) {
        super(jNetwork);
        try {
            name = jNetwork.getString("name");
            countryId = jNetwork.getString("country_id");
            country = Country.getByID(countryId);
        } catch (Exception e) {
            country = null;
        }
    }

    private static void add(Network network) {
        if (!NETWORKS.contains(network)) {
            NETWORKS.add(network);
        }
    }

    public static boolean contains(Network network) {
        return NETWORKS.contains(network);
    }

    public static Network getByNetwork(Network network) {
        for (Network n : NETWORKS) {
            if (n.equals(network)) return n;
        }
        throw new RuntimeException("This network doesn't contains in networks list");
    }

    public static Network getByID(String id) throws RuntimeException {
        for (Network n : NETWORKS) {
            if (n.id() == Integer.parseInt(id)) {
                return n;
            }
        }
        throw new RuntimeException("Network with id " + id + " doesnt contains in networks list");
    }

    public static void init(JSONObject data) throws JSONException {
        JSONArray networks = data.getJSONArray("network");
        int lNetworks = networks.length();
        for (int i = 0; i < lNetworks; i++) {
            Network.add(new Network(networks.getJSONObject(i)));
        }
    }

    public String name() {
        return name;
    }
    public Country country() { return country; }

    protected Network(Parcel in) {
        super(in);
        name = in.readString();
        countryId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(name);
        dest.writeString(countryId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Network> CREATOR = new Creator<Network>() {
        @Override
        public Network createFromParcel(Parcel in) {
            return new Network(in);
        }

        @Override
        public Network[] newArray(int size) {
            return new Network[size];
        }
    };
}

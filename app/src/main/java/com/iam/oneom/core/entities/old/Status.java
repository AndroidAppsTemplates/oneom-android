package com.iam.oneom.core.entities.old;

import android.os.Parcel;
import android.os.Parcelable;

import com.iam.oneom.core.entities.interfaces.Named;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Status extends Entity implements Parcelable, Named {

    public static String UNKNOWN_STATUS_ID = "13";
    private static ArrayList<Status> STATUSES = new ArrayList<>();
    private String name;

    public Status(JSONObject jStatus) {
        super(jStatus);
        try {
            name = jStatus.getString("name");
        } catch (JSONException e) {
//            OneOm.handleError(Thread.currentThread(), e, "Status main constructor");
            e.printStackTrace(); }
    }

    private static void add(Status status) {
        if (!STATUSES.contains(status)) {
            STATUSES.add(status);
        }
    }

    public static boolean contains(Status status) {
        return STATUSES.contains(status);
    }

    public static Status getByStatus(Status status) throws RuntimeException {
        for (Status s : STATUSES) {
            if (s.equals(status)) return s;
        }
        throw new RuntimeException("This status doesn't contains in statuses list");
    }

    public static Status getByID(String id) throws RuntimeException {
        if (id.equals("null")) id = UNKNOWN_STATUS_ID;
        for (Status s : STATUSES) {
            if (s.id() == Integer.parseInt(id)) {
                return s;
            }
        }
        throw new RuntimeException("Status with id " + id + " doesnt contains in statuses list");
    }

    public static void init(JSONObject data) throws JSONException {
        JSONArray statuses = data.getJSONArray("status");
        int lStatuses = statuses.length();
        for (int i = 0; i < lStatuses; i++) {
            Status status = new Status(statuses.getJSONObject(i));
            Status.add(status);
            if (status.getName().equalsIgnoreCase("unknown")) UNKNOWN_STATUS_ID = status.id() + "";
        }
    }

    protected Status(Parcel in) {
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

    public static final Creator<Status> CREATOR = new Creator<Status>() {
        @Override
        public Status createFromParcel(Parcel in) {
            return new Status(in);
        }

        @Override
        public Status[] newArray(int size) {
            return new Status[size];
        }
    };

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        return ((Status)o).id() == this.id();
    }
}

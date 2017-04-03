package com.iam.oneom.core;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

public enum GsonMapper {

    INSTANCE;

    public final Gson gson;

    GsonMapper() {
        gson = new GsonBuilder()
                .create();
    }

    @Nullable
    public JSONObject toJson(Object object) {
        try {
            return new JSONObject(gson.toJson(object));
        } catch (JSONException pE) {
            pE.printStackTrace();
        }
        return null;
    }

    public <T> T fromJson(final @Nullable String jString, Class<T> classOfT) {
        if (jString == null) {
            return null;
        }
        return gson.fromJson(jString, classOfT);
    }

    public <T> T fromJson(final @Nullable JSONObject jsonObject, Class<T> classOfT) {
        if (jsonObject == null) {
            return null;
        }
        return gson.fromJson(jsonObject.toString(), classOfT);
    }
}
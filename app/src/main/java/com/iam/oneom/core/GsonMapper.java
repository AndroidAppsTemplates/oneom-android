package com.iam.oneom.core;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iam.oneom.core.entities.deserializer.CountryDeserializer;
import com.iam.oneom.core.entities.model.Country;
import com.iam.oneom.core.entities.serializer.CountrySerializer;

import org.json.JSONException;
import org.json.JSONObject;

public enum GsonMapper {

    INSTANCE;

    public final Gson gson;

    GsonMapper() {
        try {
            gson = new GsonBuilder()
                    .registerTypeAdapter(Class.forName(Country.class.getCanonicalName()), new CountrySerializer())
                    .registerTypeAdapter(Class.forName(Country.class.getCanonicalName()), new CountryDeserializer())
                    .create();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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
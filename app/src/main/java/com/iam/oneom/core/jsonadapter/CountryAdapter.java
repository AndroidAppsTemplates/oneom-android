package com.iam.oneom.core.jsonadapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.model.Country;

import java.io.IOException;

/**
 * Created by iam on 03.04.17.
 */

public final class CountryAdapter extends TypeAdapter<Country> {

    @Override
    public Country read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        return DbHelper.where(Country.class).equalTo("id", getId(in)).findFirst();
    }

    private long getId(JsonReader in) {
        try {
            return Long.parseLong(in.nextString());
        } catch (IOException ignored) {
        }

        try {
            return in.nextLong();
        } catch (IOException ignored) {
        }

        return 0L;
    }

    @Override
    public synchronized void write(JsonWriter out, Country value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(value.getId());
    }
}
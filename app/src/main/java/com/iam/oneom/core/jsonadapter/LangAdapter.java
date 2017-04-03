package com.iam.oneom.core.jsonadapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.model.Lang;

import java.io.IOException;

/**
 * Created by iam on 03.04.17.
 */

public final class LangAdapter extends TypeAdapter<Lang> {

    @Override
    public Lang read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        return DbHelper.where(Lang.class).equalTo("id", in.nextLong()).findFirst();
    }

    @Override
    public synchronized void write(JsonWriter out, Lang value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(value.getId());
    }
}
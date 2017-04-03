package com.iam.oneom.core.jsonadapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.model.QualityGroup;

import java.io.IOException;

/**
 * Created by iam on 03.04.17.
 */

public final class QualityGroupAdapter extends TypeAdapter<QualityGroup> {

    @Override
    public QualityGroup read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        return DbHelper.where(QualityGroup.class).equalTo("id", in.nextLong()).findFirst();
    }

    @Override
    public synchronized void write(JsonWriter out, QualityGroup value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(value.getId());
    }
}
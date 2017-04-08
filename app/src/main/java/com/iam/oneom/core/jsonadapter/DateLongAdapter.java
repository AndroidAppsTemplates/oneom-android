package com.iam.oneom.core.jsonadapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.iam.oneom.core.util.Time;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by iam on 03.04.17.
 */

public final class DateLongAdapter extends TypeAdapter<Long> {

    private final SimpleDateFormat simpleDateFormat = Time.TimeFormat.IDN.dateFormat();
    private final SimpleDateFormat changeDateFormat = Time.TimeFormat.OutputDT.dateFormat();

    @Override
    public Long read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return deserializeToDate(in.nextString());
    }

    private synchronized Long deserializeToDate(String json) {
        Date parse;
        try {
            parse = simpleDateFormat.parse(json);
            return parse.getTime();
        } catch (ParseException ignored) {
            ignored.printStackTrace();
        }
        try {
            parse = changeDateFormat.parse(json);
            return parse.getTime();
        } catch (ParseException ignored) {
            ignored.printStackTrace();
        }

        return 0L;
    }

    @Override
    public synchronized void write(JsonWriter out, Long value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        String dateFormatAsString = simpleDateFormat.format(value);
        out.value(dateFormatAsString);
    }
}
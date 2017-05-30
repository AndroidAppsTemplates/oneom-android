package com.iam.oneom.core.jsonadapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.iam.oneom.util.Time;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by iam on 03.04.17.
 */

public final class DateAdapter extends TypeAdapter<Date> {

    private final SimpleDateFormat simpleDateFormat = Time.TimeFormat.IDN.dateFormat();
    private final SimpleDateFormat changeDateFormat = Time.TimeFormat.OutputDT.dateFormat();

    @Override
    public Date read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return deserializeToDate(in.nextString());
    }

    private synchronized Date deserializeToDate(String json) {
        Date parse;
        try {
            parse = simpleDateFormat.parse(json);
            return parse;
        } catch (ParseException ignored) {
            ignored.printStackTrace();
        }
        try {
            parse = changeDateFormat.parse(json);
            return parse;
        } catch (ParseException ignored) {
            ignored.printStackTrace();
        }

        return new Date();
    }

    @Override
    public synchronized void write(JsonWriter out, Date value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        String dateFormatAsString = simpleDateFormat.format(value);
        out.value(dateFormatAsString);
    }
}
package com.iam.oneom.core.entities.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.iam.oneom.core.entities.model.Lang;
import com.iam.oneom.core.entities.model.Source;
import com.iam.oneom.core.entities.model.Subtitle;

import java.lang.reflect.Type;

public class SubtitleDeserializer implements JsonDeserializer<Subtitle> {

    @Override
    public Subtitle deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final Subtitle subtitle = new Subtitle();

        final JsonObject json = (JsonObject) jsonElement;
        JsonElement tmpElem;
        if ((tmpElem = json.get("id")) != null && !tmpElem.isJsonNull()) {
            subtitle.setId(tmpElem.getAsLong());
        }

        if ((tmpElem = json.get("title")) != null && !tmpElem.isJsonNull()) {
            subtitle.setTitle(tmpElem.getAsString());
        }

        if ((tmpElem = json.get("lang")) != null && !tmpElem.isJsonNull()) {
            subtitle.setLang(context.deserialize(tmpElem, Lang.class));
        }

        if ((tmpElem = json.get("source")) != null && !tmpElem.isJsonNull()) {
            subtitle.setSource(context.deserialize(tmpElem, Source.class));
        }

        if ((tmpElem = json.get("file_id")) != null && !tmpElem.isJsonNull()) {
            subtitle.setFileId(tmpElem.getAsInt());
        }

        if ((tmpElem = json.get("assoc_id")) != null && !tmpElem.isJsonNull()) {
            subtitle.setAssocId(tmpElem.getAsInt());
        }

        if ((tmpElem = json.get("assoc_type")) != null && !tmpElem.isJsonNull()) {
            subtitle.setAssocType(tmpElem.getAsString());
        }

        return subtitle;
    }

}
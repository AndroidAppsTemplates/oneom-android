package com.iam.oneom.core.entities.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.iam.oneom.core.entities.model.Lang;

import java.lang.reflect.Type;

public class LangDeserializer implements JsonDeserializer<Lang> {

    @Override
    public Lang deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final Lang lang = new Lang();
        final JsonObject json = (JsonObject) jsonElement;
        JsonElement tmpElem;
        if ((tmpElem = json.get("id")) != null && !tmpElem.isJsonNull()) {
            lang.setId(tmpElem.getAsLong());
        }
        if ((tmpElem = json.get("name")) != null && !tmpElem.isJsonNull()) {
            lang.setName(tmpElem.getAsString());
        }
        if ((tmpElem = json.get("opensubtitles_short")) != null && !tmpElem.isJsonNull()) {
            lang.setOpenSubtitlesShort(tmpElem.getAsString());
        }
        if ((tmpElem = json.get("short")) != null && !tmpElem.isJsonNull()) {
            lang.setShortName(tmpElem.getAsString());
        }
        return lang;
    }

}
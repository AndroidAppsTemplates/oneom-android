package com.iam.oneom.core.entities.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.iam.oneom.core.entities.model.Genre;
import com.iam.oneom.core.entities.model.Lang;

import java.lang.reflect.Type;

public class LangSerializer implements JsonSerializer<Lang> {

    @Override
    public JsonElement serialize(Lang lang, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", lang.getId());
        jsonObject.addProperty("name", lang.getName());
        jsonObject.addProperty("short_name", lang.getShortName());
        jsonObject.addProperty("open_subtitles_short", lang.getOpenSubtitlesShort());

        return jsonObject;
    }
}
package com.iam.oneom.core.entities.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.iam.oneom.core.entities.model.File;
import com.iam.oneom.core.entities.model.Genre;

import java.lang.reflect.Type;

public class GenreSerializer implements JsonSerializer<Genre> {

    @Override
    public JsonElement serialize(Genre genre, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", genre.getId());
        jsonObject.addProperty("name", genre.getName());

        return jsonObject;
    }
}
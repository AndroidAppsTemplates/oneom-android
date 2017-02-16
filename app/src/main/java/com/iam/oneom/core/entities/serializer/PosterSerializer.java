package com.iam.oneom.core.entities.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.iam.oneom.core.entities.model.Poster;

import java.lang.reflect.Type;

public class PosterSerializer implements JsonSerializer<Poster> {

    @Override
    public JsonElement serialize(Poster poster, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", poster.getId());
        jsonObject.addProperty("name", poster.getName());
        jsonObject.addProperty("alt", poster.getAlt());
        jsonObject.addProperty("description", poster.getDescription());
        jsonObject.addProperty("original", poster.getOriginal());
        jsonObject.addProperty("path", poster.getPath());
        jsonObject.addProperty("type", poster.getType());
        jsonObject.addProperty("size", poster.getSize());
        jsonObject.addProperty("tint_color", poster.getTintColor());

        return jsonObject;
    }
}
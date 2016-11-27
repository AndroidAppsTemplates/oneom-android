package com.iam.oneom.core.entities.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.iam.oneom.core.entities.model.File;

import java.lang.reflect.Type;

public class FileSerializer implements JsonSerializer<File> {

    @Override
    public JsonElement serialize(File file, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", file.getId());
        jsonObject.addProperty("name", file.getName());
        jsonObject.addProperty("alt", file.getAlt());
        jsonObject.addProperty("description", file.getDescription());
        jsonObject.addProperty("original", file.getOriginal());
        jsonObject.addProperty("path", file.getPath());
        jsonObject.addProperty("type", file.getPath());
        jsonObject.addProperty("size", file.getSize());

        return jsonObject;
    }
}
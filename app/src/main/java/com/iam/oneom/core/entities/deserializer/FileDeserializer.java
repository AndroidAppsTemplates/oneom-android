package com.iam.oneom.core.entities.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.iam.oneom.core.entities.model.File;

import java.lang.reflect.Type;

public class FileDeserializer implements JsonDeserializer<File> {

    @Override
    public File deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        File file = new File();

        final JsonObject jsonElement = (JsonObject) json;
        JsonElement tmpElem;

        if ((tmpElem = jsonElement.get("id")) != null && !tmpElem.isJsonNull()) {
            file.setId(tmpElem.getAsLong());
        }

        if ((tmpElem = jsonElement.get("name")) != null && !tmpElem.isJsonNull()) {
            file.setName(tmpElem.getAsString());
        }

        if ((tmpElem = jsonElement.get("alt")) != null && !tmpElem.isJsonNull()) {
            file.setAlt(tmpElem.getAsString());
        }

        if ((tmpElem = jsonElement.get("description")) != null && !tmpElem.isJsonNull()) {
            file.setDescription(tmpElem.getAsString());
        }

        if ((tmpElem = jsonElement.get("original")) != null && !tmpElem.isJsonNull()) {
            file.setOriginal(tmpElem.getAsString());
        }

        if ((tmpElem = jsonElement.get("path")) != null && !tmpElem.isJsonNull()) {
            file.setPath(tmpElem.getAsString());
        }

        if ((tmpElem = jsonElement.get("type")) != null && !tmpElem.isJsonNull()) {
            file.setType(tmpElem.getAsString());
        }

        if ((tmpElem = jsonElement.get("size")) != null && !tmpElem.isJsonNull()) {
            file.setSize(tmpElem.getAsLong());
        }


        return file;
    }
}

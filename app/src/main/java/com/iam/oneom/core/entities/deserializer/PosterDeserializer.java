package com.iam.oneom.core.entities.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.iam.oneom.core.entities.model.Poster;

import java.lang.reflect.Type;

public class PosterDeserializer implements JsonDeserializer<Poster> {

    @Override
    public Poster deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final Poster poster = new Poster();
        final JsonObject json = (JsonObject) jsonElement;
        JsonElement tmpElem;
        if ((tmpElem = json.get("id")) != null && !tmpElem.isJsonNull()) {
            poster.setId(tmpElem.getAsLong());
        }
        if ((tmpElem = json.get("name")) != null && !tmpElem.isJsonNull()) {
            poster.setName(tmpElem.getAsString());
        }
        if ((tmpElem = json.get("alt")) != null && !tmpElem.isJsonNull()) {
            poster.setAlt(tmpElem.getAsString());
        }
        if ((tmpElem = json.get("description")) != null && !tmpElem.isJsonNull()) {
            poster.setDescription(tmpElem.getAsString());
        }
        if ((tmpElem = json.get("original")) != null && !tmpElem.isJsonNull()) {
            poster.setOriginal(tmpElem.getAsString());
        }
        if ((tmpElem = json.get("name")) != null && !tmpElem.isJsonNull()) {
            poster.setName(tmpElem.getAsString());
        }
        if ((tmpElem = json.get("path")) != null && !tmpElem.isJsonNull()) {
            poster.setPath(tmpElem.getAsString());
        }
        if ((tmpElem = json.get("type")) != null && !tmpElem.isJsonNull()) {
            poster.setType(tmpElem.getAsString());
        }
        if ((tmpElem = json.get("size")) != null && !tmpElem.isJsonNull()) {
            poster.setSize(tmpElem.getAsLong());
        }
        if ((tmpElem = json.get("tint_color")) != null && !tmpElem.isJsonNull()) {
            poster.setTintColor(tmpElem.getAsInt());
        }
        return poster;
    }


//    private long id;
//    private String name;
//    private String alt;
//    private String description;
//    private String original;
//    private String path;
//    private String type;
//    private long size;
}
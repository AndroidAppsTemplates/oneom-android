package com.iam.oneom.core.entities.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.iam.oneom.core.entities.model.Description;

import java.lang.reflect.Type;

public class DescriptionDeserializer implements JsonDeserializer<Description> {

    @Override
    public Description deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final Description description = new Description();
        final JsonObject json = (JsonObject) jsonElement;
        JsonElement tmpElem;
        if ((tmpElem = json.get("id")) != null && !tmpElem.isJsonNull()) {
            description.setId(tmpElem.getAsLong());
        }
        if ((tmpElem = json.get("assoc_id")) != null && !tmpElem.isJsonNull()) {
            description.setAssocId(tmpElem.getAsLong());
        }
        if ((tmpElem = json.get("assoc_type")) != null && !tmpElem.isJsonNull()) {
            description.setAssocType(tmpElem.getAsString());
        }
        if ((tmpElem = json.get("body")) != null && !tmpElem.isJsonNull()) {
            description.setBody(tmpElem.getAsString());
        }
        if ((tmpElem = json.get("type_id")) != null && !tmpElem.isJsonNull()) {
            description.setTypeId(tmpElem.getAsLong());
        }
        if ((tmpElem = json.get("lang_id")) != null && !tmpElem.isJsonNull()) {
            description.setLangId(tmpElem.getAsLong());
        }
        return description;
    }

}
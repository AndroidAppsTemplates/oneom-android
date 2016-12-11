package com.iam.oneom.core.entities.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.iam.oneom.core.entities.model.Status;
import com.iam.oneom.core.network.request.SerialSearchResult;

import java.lang.reflect.Type;

public class SerialSearchResultDeserializer implements JsonDeserializer<SerialSearchResult> {

    @Override
    public SerialSearchResult deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final SerialSearchResult serialSearchResult = new SerialSearchResult();
        final JsonObject json = (JsonObject) jsonElement;
        JsonElement tmpElem;
        if ((tmpElem = json.get("id")) != null && !tmpElem.isJsonNull()) {
            serialSearchResult.setId(tmpElem.getAsLong());
        }
        if ((tmpElem = json.get("title")) != null && !tmpElem.isJsonNull()) {
            serialSearchResult.setTitle(tmpElem.getAsString());
        }
        return serialSearchResult;
    }
}

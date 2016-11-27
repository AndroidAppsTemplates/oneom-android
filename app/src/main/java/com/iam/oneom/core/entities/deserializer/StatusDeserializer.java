package com.iam.oneom.core.entities.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.iam.oneom.core.entities.model.Status;

import java.lang.reflect.Type;

public class StatusDeserializer implements JsonDeserializer<Status> {

    @Override
    public Status deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final Status status = new Status();
        final JsonObject json = (JsonObject) jsonElement;
        JsonElement tmpElem;
        if ((tmpElem = json.get("id")) != null && !tmpElem.isJsonNull()) {
            status.setId(tmpElem.getAsLong());
        }
        if ((tmpElem = json.get("name")) != null && !tmpElem.isJsonNull()) {
            status.setName(tmpElem.getAsString());
        }
        return status;
    }

}
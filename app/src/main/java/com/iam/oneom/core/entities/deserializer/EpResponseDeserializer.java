package com.iam.oneom.core.entities.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.network.response.EpResponse;

import java.lang.reflect.Type;

public class EpResponseDeserializer implements JsonDeserializer<EpResponse> {
    @Override
    public EpResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        EpResponse response = new EpResponse();

        final JsonObject jsonObject = (JsonObject) json;

        JsonElement tmpElem;

        if ((tmpElem = jsonObject.get("ep")) != null && !tmpElem.isJsonNull()) {
            response.setEpisode(context.deserialize(tmpElem, Episode.class));
        }

        return response;
    }
}

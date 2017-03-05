package com.iam.oneom.core.entities.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.iam.oneom.core.entities.model.Serial;
import com.iam.oneom.core.network.response.SerialResponse;

import java.lang.reflect.Type;

public class SerialResponseDeserializer implements JsonDeserializer<SerialResponse> {
    @Override
    public SerialResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        SerialResponse request = new SerialResponse();

        final JsonObject jsonObject = (JsonObject) json;

        JsonElement tmpElem;

        if ((tmpElem = jsonObject.get("serial")) != null && !tmpElem.isJsonNull()) {
            request.setSerial(context.deserialize(tmpElem, Serial.class));
        }

        return request;
    }
}

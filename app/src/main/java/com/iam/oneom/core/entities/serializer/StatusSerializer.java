package com.iam.oneom.core.entities.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.iam.oneom.core.entities.model.Source;
import com.iam.oneom.core.entities.model.Status;

import java.lang.reflect.Type;

public class StatusSerializer implements JsonSerializer<Status> {


    @Override
    public JsonElement serialize(Status status, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", status.getId());
        jsonObject.addProperty("name", status.getName());

        return jsonObject;
    }
}
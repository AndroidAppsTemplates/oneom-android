package com.iam.oneom.core.entities.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.iam.oneom.core.entities.model.Country;
import com.iam.oneom.core.entities.model.Lang;
import com.iam.oneom.core.entities.model.Network;

import java.lang.reflect.Type;

public class NetworkSerializer implements JsonSerializer<Network> {

    @Override
    public JsonElement serialize(Network network, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", network.getId());
        jsonObject.addProperty("name", network.getName());
        jsonObject.addProperty("country_id", network.getCountry() == null ? 0 : network.getCountry().getId());

        return jsonObject;
    }
}
package com.iam.oneom.core.entities.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.iam.oneom.core.entities.model.Country;

import java.lang.reflect.Type;

public class CountrySerializer implements JsonSerializer<Country> {

    @Override
    public JsonElement serialize(Country country, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", country.getId());
        jsonObject.addProperty("name", country.getName());
        return jsonObject;
    }
}
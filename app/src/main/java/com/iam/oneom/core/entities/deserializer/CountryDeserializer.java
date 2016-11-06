package com.iam.oneom.core.entities.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.iam.oneom.core.entities.model.Country;

import java.lang.reflect.Type;

public class CountryDeserializer implements JsonDeserializer<Country> {

    @Override
    public Country deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final Country country = new Country();
        final JsonObject json = (JsonObject) jsonElement;
        JsonElement tmpElem;
        if ((tmpElem = json.get("id")) != null && !tmpElem.isJsonNull()) {
            country.setId(tmpElem.getAsLong());
        }
        if ((tmpElem = json.get("name")) != null && !tmpElem.isJsonNull()) {
            country.setName(tmpElem.getAsString());
        }
        return country;
    }
}
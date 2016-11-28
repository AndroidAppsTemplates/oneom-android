package com.iam.oneom.core.entities.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.iam.oneom.core.entities.model.Country;
import com.iam.oneom.core.entities.model.Description;
import com.iam.oneom.core.entities.model.Lang;

import java.lang.reflect.Type;

public class DescriptionSerializer implements JsonSerializer<Description> {

    @Override
    public JsonElement serialize(Description description, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", description.getId());
        jsonObject.addProperty("assoc_id", description.getAssocId());
        jsonObject.addProperty("assoc_type", description.getAssocType());
        jsonObject.addProperty("type_id", description.getTypeId());
        jsonObject.addProperty("lang_id", description.getLang());
        return jsonObject;
    }
}
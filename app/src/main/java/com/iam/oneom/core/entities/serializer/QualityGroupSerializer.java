package com.iam.oneom.core.entities.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.iam.oneom.core.entities.model.Quality;
import com.iam.oneom.core.entities.model.QualityGroup;

import java.lang.reflect.Type;

public class QualityGroupSerializer implements JsonSerializer<QualityGroup> {

//    private long id;
//    private String name;
//    private QualityGroup qualityGroup;

    @Override
    public JsonElement serialize(QualityGroup qualityGroup, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", qualityGroup.getId());
        jsonObject.addProperty("name", qualityGroup.getName());

        return jsonObject;
    }
}
package com.iam.oneom.core.entities.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.iam.oneom.core.entities.model.Poster;
import com.iam.oneom.core.entities.model.Quality;
import com.iam.oneom.core.entities.model.QualityGroup;

import java.lang.reflect.Type;

public class QualitySerializer implements JsonSerializer<Quality> {

//    private long id;
//    private String name;
//    private QualityGroup qualityGroup;

    @Override
    public JsonElement serialize(Quality quality, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", quality.getId());
        jsonObject.addProperty("name", quality.getName());
        jsonObject.addProperty("quality_group_id", quality.getQualityGroup() == null ? 0 : quality.getQualityGroup().getId());

        return jsonObject;
    }
}
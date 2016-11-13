package com.iam.oneom.core.entities.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.iam.oneom.core.entities.model.Poster;
import com.iam.oneom.core.entities.model.Quality;
import com.iam.oneom.core.entities.model.QualityGroup;

import java.lang.reflect.Type;

import io.realm.Realm;

public class QualityDeserializer implements JsonDeserializer<Quality> {

    @Override
    public Quality deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final Quality quality = new Quality();
        final JsonObject json = (JsonObject) jsonElement;
        JsonElement tmpElem;
        Realm realm = Realm.getDefaultInstance();
        if ((tmpElem = json.get("id")) != null && !tmpElem.isJsonNull()) {
            quality.setId(tmpElem.getAsLong());
        }
        if ((tmpElem = json.get("name")) != null && !tmpElem.isJsonNull()) {
            quality.setName(tmpElem.getAsString());
        }
        if ((tmpElem = json.get("quality_group_id")) != null && !tmpElem.isJsonNull()) {
            if (realm != null) {
                quality.setQualityGroup(realm.where(QualityGroup.class).equalTo("id", tmpElem.getAsLong()).findFirst());
            }
        }

        realm.close();
        return quality;
    }


//    private long id;
//    private String name;
//    private String alt;
//    private String description;
//    private String original;
//    private String path;
//    private String type;
//    private long size;
}
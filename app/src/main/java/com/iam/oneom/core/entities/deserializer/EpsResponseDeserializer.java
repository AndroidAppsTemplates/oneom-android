package com.iam.oneom.core.entities.deserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.network.response.EpsResponse;

import java.lang.reflect.Type;

import io.realm.RealmList;

public class EpsResponseDeserializer implements JsonDeserializer<EpsResponse> {

    @Override
    public EpsResponse deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        EpsResponse request = new EpsResponse();

        final JsonObject jsonObject = (JsonObject) jsonElement;

        JsonArray cArray = (JsonArray) jsonObject.get("eps");
        if (cArray != null && !cArray.isJsonNull()) {
            final RealmList<Episode> episodes = new RealmList<>();
            for (int i = 0; i < cArray.size(); ++i) {
                episodes.add(context.deserialize(cArray.get(i), Episode.class));
            }
            request.setEps(episodes);
        }


        return request;
    }

}
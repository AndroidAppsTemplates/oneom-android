package com.iam.oneom.core.entities.deserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.iam.oneom.core.entities.model.Country;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.entities.model.Genre;
import com.iam.oneom.core.entities.model.Network;
import com.iam.oneom.core.entities.model.Quality;
import com.iam.oneom.core.entities.model.QualityGroup;
import com.iam.oneom.core.entities.model.Source;
import com.iam.oneom.core.entities.model.Status;
import com.iam.oneom.core.network.request.DataConfigRequest;
import com.iam.oneom.core.network.request.EpsRequest;

import java.lang.reflect.Type;

import io.realm.Realm;
import io.realm.RealmList;

public class EpsRequestDeserializer implements JsonDeserializer<EpsRequest> {

    @Override
    public EpsRequest deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        EpsRequest request = new EpsRequest();

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
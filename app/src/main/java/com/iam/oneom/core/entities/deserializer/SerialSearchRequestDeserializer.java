package com.iam.oneom.core.entities.deserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.iam.oneom.core.network.request.SerialSearchResult;
import com.iam.oneom.core.network.request.SerialsSearchRequest;

import java.lang.reflect.Type;

import io.realm.RealmList;


public class SerialSearchRequestDeserializer implements JsonDeserializer<SerialsSearchRequest> {
    @Override
    public SerialsSearchRequest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final SerialsSearchRequest serialsSearchRequest = new SerialsSearchRequest();
        final JsonObject jsonElement = (JsonObject) json;
        JsonElement tmpElem;
        if ((tmpElem = jsonElement.get("text")) != null && !tmpElem.isJsonNull()) {
            serialsSearchRequest.setText(tmpElem.getAsString());
        }

        JsonArray sArray = (JsonArray) jsonElement.get("serials");
        if (sArray != null && !sArray.isJsonNull()) {
            final RealmList<SerialSearchResult> serialSearchResults = new RealmList<>();
            for (int i = 0; i < sArray.size(); ++i) {
                serialSearchResults.add(context.deserialize(sArray.get(i), SerialSearchResult.class));
            }
            serialsSearchRequest.setResults(serialSearchResults);
        }

        return serialsSearchRequest;
    }
}

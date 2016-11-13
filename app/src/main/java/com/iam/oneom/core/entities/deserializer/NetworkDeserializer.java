package com.iam.oneom.core.entities.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.iam.oneom.core.entities.model.Country;
import com.iam.oneom.core.entities.model.Lang;
import com.iam.oneom.core.entities.model.Network;

import java.lang.reflect.Type;

import io.realm.Realm;

public class NetworkDeserializer implements JsonDeserializer<Network> {

    @Override
    public Network deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final Network network = new Network();
        final JsonObject json = (JsonObject) jsonElement;
        JsonElement tmpElem;
        Realm realm = Realm.getDefaultInstance();
        if ((tmpElem = json.get("id")) != null && !tmpElem.isJsonNull()) {
            network.setId(tmpElem.getAsLong());
        }
        if ((tmpElem = json.get("name")) != null && !tmpElem.isJsonNull()) {
            network.setName(tmpElem.getAsString());
        }
        if ((tmpElem = json.get("country_id")) != null && !tmpElem.isJsonNull()) {
            if (realm != null) {
                network.setCountry(realm.where(Country.class).equalTo("id", tmpElem.getAsLong()).findFirst());
            }
        }
        realm.close();
        return network;
    }

}
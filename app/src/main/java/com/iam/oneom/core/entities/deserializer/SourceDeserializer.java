package com.iam.oneom.core.entities.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.iam.oneom.core.entities.model.Network;
import com.iam.oneom.core.entities.model.Source;

import java.lang.reflect.Type;

import io.realm.Realm;

public class SourceDeserializer implements JsonDeserializer<Source> {

    @Override
    public Source deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final Source source = new Source();
        final JsonObject json = (JsonObject) jsonElement;
        JsonElement tmpElem;

        Realm realm = Realm.getDefaultInstance();

        if ((tmpElem = json.get("id")) != null && !tmpElem.isJsonNull()) {
            source.setId(tmpElem.getAsLong());
        }

        if ((tmpElem = json.get("active")) != null && !tmpElem.isJsonNull()) {
            source.setActive(tmpElem.getAsInt());
        }

        if ((tmpElem = json.get("name")) != null && !tmpElem.isJsonNull()) {
            source.setName(tmpElem.getAsString());
        }

        if ((tmpElem = json.get("url")) != null && !tmpElem.isJsonNull()) {
            source.setUrl(tmpElem.getAsString());
        }

        if ((tmpElem = json.get("search")) != null && !tmpElem.isJsonNull()) {
            source.setSearch(tmpElem.getAsString());
        }

        if ((tmpElem = json.get("search_page")) != null && !tmpElem.isJsonNull()) {
            source.setSearchPage(tmpElem.getAsString());
        }

        if ((tmpElem = json.get("search_step")) != null && !tmpElem.isJsonNull()) {
            source.setSearchPage(tmpElem.getAsString());
        }

        if ((tmpElem = json.get("data")) != null && !tmpElem.isJsonNull()) {
            source.setData(tmpElem.getAsString());
        }

        if ((tmpElem = json.get("login")) != null && !tmpElem.isJsonNull()) {
            source.setLogin(tmpElem.getAsInt());
        }

        if ((tmpElem = json.get("type_id")) != null && !tmpElem.isJsonNull()) {
            if (realm != null) {
                source.setType(tmpElem.getAsInt());
            }
        }



        realm.close();
        return source;
    }

}
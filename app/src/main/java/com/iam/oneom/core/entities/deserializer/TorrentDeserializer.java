package com.iam.oneom.core.entities.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.iam.oneom.core.entities.model.Lang;
import com.iam.oneom.core.entities.model.Quality;
import com.iam.oneom.core.entities.model.Source;
import com.iam.oneom.core.entities.model.Subtitle;
import com.iam.oneom.core.entities.model.Torrent;

import java.lang.reflect.Type;

import io.realm.Realm;

public class TorrentDeserializer implements JsonDeserializer<Torrent> {

    @Override
    public Torrent deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final Torrent torrent = new Torrent();
        final Realm realm = Realm.getDefaultInstance();

        final JsonObject json = (JsonObject) jsonElement;
        JsonElement tmpElem;
        if ((tmpElem = json.get("id")) != null && !tmpElem.isJsonNull()) {
            torrent.setId(tmpElem.getAsLong());
        }

        if ((tmpElem = json.get("title")) != null && !tmpElem.isJsonNull()) {
            torrent.setTitle(tmpElem.getAsString());
        }

        if ((tmpElem = json.get("lang_id")) != null && !tmpElem.isJsonNull()) {
            torrent.setLang(realm.where(Lang.class).equalTo("id", tmpElem.getAsLong()).findFirst());
        }

        if ((tmpElem = json.get("source_id")) != null && !tmpElem.isJsonNull()) {
            torrent.setSource(realm.where(Source.class).equalTo("id", tmpElem.getAsLong()).findFirst());
        }

        if ((tmpElem = json.get("quality_id")) != null && !tmpElem.isJsonNull()) {
            torrent.setQuality(realm.where(Quality.class).equalTo("id", tmpElem.getAsLong()).findFirst());
        }

        if ((tmpElem = json.get("vk_post_id")) != null && !tmpElem.isJsonNull()) {
            torrent.setVkPostId(tmpElem.getAsString());
        }

        if ((tmpElem = json.get("value")) != null && !tmpElem.isJsonNull()) {
            torrent.setValue(tmpElem.getAsString());
        }

        if ((tmpElem = json.get("url")) != null && !tmpElem.isJsonNull()) {
            torrent.setUrl(tmpElem.getAsString());
        }

        realm.close();
        return torrent;
    }

}
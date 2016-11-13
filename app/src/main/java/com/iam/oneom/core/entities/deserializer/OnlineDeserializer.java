package com.iam.oneom.core.entities.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.iam.oneom.core.entities.model.Lang;
import com.iam.oneom.core.entities.model.Network;
import com.iam.oneom.core.entities.model.Online;
import com.iam.oneom.core.entities.model.Quality;
import com.iam.oneom.core.entities.model.Source;

import java.lang.reflect.Type;

import io.realm.Realm;

public class OnlineDeserializer implements JsonDeserializer<Online> {

    @Override
    public Online deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final Online online = new Online();
        final JsonObject json = (JsonObject) jsonElement;
        JsonElement tmpElem;
        Realm realm = Realm.getDefaultInstance();
        if ((tmpElem = json.get("id")) != null && !tmpElem.isJsonNull()) {
            online.setId(tmpElem.getAsLong());
        }
        if ((tmpElem = json.get("title")) != null && !tmpElem.isJsonNull()) {
            online.setTitle(tmpElem.getAsString());
        }
        if ((tmpElem = json.get("embed_code")) != null && !tmpElem.isJsonNull()) {
            online.setEmbedCode(tmpElem.getAsString());
        }
        if ((tmpElem = json.get("url")) != null && !tmpElem.isJsonNull()) {
            online.setUrl(tmpElem.getAsString());
        }
        if ((tmpElem = json.get("video_url")) != null && !tmpElem.isJsonNull()) {
            online.setVideoUrl(tmpElem.getAsString());
        }
        if ((tmpElem = json.get("poster_url")) != null && !tmpElem.isJsonNull()) {
            online.setPosterUrl(tmpElem.getAsString());
        }
        if ((tmpElem = json.get("source_id")) != null && !tmpElem.isJsonNull()) {
            if (realm != null) {
                online.setSource(realm.where(Source.class).equalTo("id", tmpElem.getAsLong()).findFirst());
            }
        }
        if ((tmpElem = json.get("lang_id")) != null && !tmpElem.isJsonNull()) {
            if (realm != null) {
                online.setLang(realm.where(Lang.class).equalTo("id", tmpElem.getAsLong()).findFirst());
            }
        }
        if ((tmpElem = json.get("quality_id")) != null && !tmpElem.isJsonNull()) {
            if (realm != null) {
                online.setQuality(realm.where(Quality.class).equalTo("id", tmpElem.getAsLong()).findFirst());
            }
        }

        realm.close();
        return online;
    }


//    private long id;
//    private Source source;
//    private Lang lang;
//    private Quality quality;
//    private String title;
//    private String embedCode;
//    private String url;
//    private String videoUrl;
//    private String posterUrl;

}
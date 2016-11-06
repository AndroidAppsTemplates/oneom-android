package com.iam.oneom.core.entities.deserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.entities.model.Torrent;

import java.lang.reflect.Type;

import io.realm.RealmList;

public class EpisodeDeserializer implements JsonDeserializer<Episode> {

    @Override
    public Episode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Episode episode = new Episode();

        final JsonObject jsonObject = (JsonObject) json;

        JsonElement tmpElem;

        if ((tmpElem = jsonObject.get("id")) != null && !tmpElem.isJsonNull()) {
            episode.setId(tmpElem.getAsLong());
        }

        JsonArray array = (JsonArray) jsonObject.get("torrent");
        if (array != null && !array.isJsonNull()) {
            final RealmList<Torrent> torrent = new RealmList<>();
            for(int i = 0; i < array.size(); ++i) {
                torrent.add(context.deserialize(array.get(i), Torrent.class));
            }
            episode.setTorrent(torrent);
        }


        return episode;
    }


//    private int id;
//
//    private RealmList<Torrent> torrent;
//    private RealmList<Online> online;
//    private int ep;
//    private int season;
//    private Subtitle subtitle;
//    private Serial serial;
//    private String serialId;
//    private String rait;
//    private String title;
//    private String vkPostId;
//    private String description;
//    private String airdate;
//    private String createdAt;
//    private String deletedAt;
//    private String updatedAt;
//    private String videoStreamUrl;
}

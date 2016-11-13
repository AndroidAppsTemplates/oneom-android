package com.iam.oneom.core.entities.deserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.iam.oneom.core.entities.model.Description;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.entities.model.Online;
import com.iam.oneom.core.entities.model.Serial;
import com.iam.oneom.core.entities.model.Subtitle;
import com.iam.oneom.core.entities.model.Torrent;

import java.lang.reflect.Type;

import io.realm.Realm;
import io.realm.RealmList;

public class EpisodeDeserializer implements JsonDeserializer<Episode> {

    @Override
    public Episode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Episode episode = new Episode();
        Realm realm = Realm.getDefaultInstance();

        final JsonObject jsonObject = (JsonObject) json;

        JsonElement tmpElem;

        if ((tmpElem = jsonObject.get("id")) != null && !tmpElem.isJsonNull()) {
            episode.setId(tmpElem.getAsLong());
        }

        JsonArray tArray = (JsonArray) jsonObject.get("torrent");
        if (tArray != null && !tArray.isJsonNull()) {
            final RealmList<Torrent> torrent = new RealmList<>();
            for(int i = 0; i < tArray.size(); ++i) {
                torrent.add(context.deserialize(tArray.get(i), Torrent.class));
            }
            episode.setTorrent(torrent);
        }

        JsonArray oArray = (JsonArray) jsonObject.get("online");
        if (oArray != null && !oArray.isJsonNull()) {
            final RealmList<Online> online = new RealmList<>();
            for(int i = 0; i < oArray.size(); ++i) {
                online.add(context.deserialize(oArray.get(i), Torrent.class));
            }
            episode.setOnline(online);
        }

        if ((tmpElem = jsonObject.get("ep")) != null && !tmpElem.isJsonNull()) {
            episode.setEp(tmpElem.getAsString());
        }

        if ((tmpElem = jsonObject.get("season")) != null && !tmpElem.isJsonNull()) {
            episode.setSeason(tmpElem.getAsString());
        }

        JsonArray subtArray = (JsonArray) jsonObject.get("subtitle");
        if (subtArray != null && !subtArray.isJsonNull()) {
            final RealmList<Subtitle> subtitles = new RealmList<>();
            for(int i = 0; i < subtArray.size(); ++i) {
                subtitles.add(context.deserialize(subtArray.get(i), Torrent.class));
            }
            episode.setSubtitle(subtitles);
        }

        if ((tmpElem = jsonObject.get("serial_id")) != null && !tmpElem.isJsonNull()) {

            if (realm != null) {
                Serial serial = realm.where(Serial.class).equalTo("id", tmpElem.getAsLong()).findFirst();
                if (serial != null) {
                    episode.setSerial(serial);
                } else {
                    if ((tmpElem = jsonObject.get("serial")) != null && !tmpElem.isJsonNull()) {
                        episode.setSerial(context.deserialize(tmpElem, Serial.class));
                    }
                }
            }
        }

        if ((tmpElem = jsonObject.get("rait")) != null && !tmpElem.isJsonNull()) {
            episode.setRait(tmpElem.getAsString());
        }

        if ((tmpElem = jsonObject.get("title")) != null && !tmpElem.isJsonNull()) {
            episode.setTitle(tmpElem.getAsString());
        }

        if ((tmpElem = jsonObject.get("vk_post_id")) != null && !tmpElem.isJsonNull()) {
            episode.setVkPostId(tmpElem.getAsString());
        }

        JsonArray dArray = (JsonArray) jsonObject.get("description");
        if (dArray != null && !dArray.isJsonNull()) {
            final RealmList<Description> descriptions = new RealmList<>();
            for(int i = 0; i < dArray.size(); ++i) {
                descriptions.add(context.deserialize(dArray.get(i), Torrent.class));
            }
            episode.setDescription(descriptions);
        }

        if ((tmpElem = jsonObject.get("airdate")) != null && !tmpElem.isJsonNull()) {
            episode.setAirdate(tmpElem.getAsString());
        }

        if ((tmpElem = jsonObject.get("video_stream_url")) != null && !tmpElem.isJsonNull()) {
            episode.setVideoStreamUrl(tmpElem.getAsString());
        }

        realm.close();

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

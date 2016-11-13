package com.iam.oneom.core.entities.deserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.iam.oneom.core.entities.model.Country;
import com.iam.oneom.core.entities.model.Description;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.entities.model.Genre;
import com.iam.oneom.core.entities.model.Network;
import com.iam.oneom.core.entities.model.Online;
import com.iam.oneom.core.entities.model.Poster;
import com.iam.oneom.core.entities.model.QualityGroup;
import com.iam.oneom.core.entities.model.Serial;
import com.iam.oneom.core.entities.model.Status;
import com.iam.oneom.core.entities.model.Subtitle;
import com.iam.oneom.core.entities.model.Torrent;

import java.lang.reflect.Type;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;

public class SerialDeserializer implements JsonDeserializer<Serial> {

//    private int id;
//    private Poster poster;
//    private RealmList<Genre> genre;
//    private RealmList<Country> country;
//    private RealmList<Network> network;
//    private RealmList<Episode> episode;
//    private RealmList<Description> description;
//    private Status status;
//    private String title;
//    private int hide;
//    private int tvrageId;
//    private int tvmazeId;
//    private int mdbId;
//    private int tvdbId;
//    private int vkGroupId;
//    private String imbbId;
//    private String imdbRating;
//    private int runtime;
//    private Date start; // "1950-07-04",
//    private Date end; // "1950-07-04",
//    private String airtime;
//    private String airday;
//    private String timezone;
//    private String vkGroupUpdate;
//
    @Override
    public Serial deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Serial serial = new Serial();
        Realm realm = Realm.getDefaultInstance();

        final JsonObject jsonObject = (JsonObject) jsonElement;

        JsonElement tmpElem;

        if ((tmpElem = jsonObject.get("id")) != null && !tmpElem.isJsonNull()) {
            serial.setId(tmpElem.getAsLong());
        }

        JsonArray tArray = (JsonArray) jsonObject.get("torrent");
        if (tArray != null && !tArray.isJsonNull()) {
            final RealmList<Torrent> torrent = new RealmList<>();
            for(int i = 0; i < tArray.size(); ++i) {
                torrent.add(context.deserialize(tArray.get(i), Torrent.class));
            }
            serial.setTorrent(torrent);
        }

        JsonArray oArray = (JsonArray) jsonObject.get("online");
        if (oArray != null && !oArray.isJsonNull()) {
            final RealmList<Online> online = new RealmList<>();
            for(int i = 0; i < oArray.size(); ++i) {
                online.add(context.deserialize(oArray.get(i), Torrent.class));
            }
            serial.setOnline(online);
        }

        if ((tmpElem = jsonObject.get("ep")) != null && !tmpElem.isJsonNull()) {
            serial.setEp(tmpElem.getAsString());
        }

        if ((tmpElem = jsonObject.get("season")) != null && !tmpElem.isJsonNull()) {
            serial.setSeason(tmpElem.getAsString());
        }

        JsonArray subtArray = (JsonArray) jsonObject.get("subtitle");
        if (subtArray != null && !subtArray.isJsonNull()) {
            final RealmList<Subtitle> subtitles = new RealmList<>();
            for(int i = 0; i < subtArray.size(); ++i) {
                subtitles.add(context.deserialize(subtArray.get(i), Torrent.class));
            }
            serial.setSubtitle(subtitles);
        }

//        if ((tmpElem = jsonObject.get("serial_id")) != null && !tmpElem.isJsonNull()) {
//
//            if (realm != null) {
//                Serial serial = realm.where(Serial.class).equalTo("id", tmpElem.getAsLong()).findFirst();
//                if (serial != null) {
//                    episode.setSerial(serial);
//                } else {
//                    if ((tmpElem = jsonObject.get("serial")) != null && !tmpElem.isJsonNull()) {
//                        episode.setSerial(context.deserialize(tmpElem, Serial.class));
//                    }
//                }
//            }
//        }

        if ((tmpElem = jsonObject.get("rait")) != null && !tmpElem.isJsonNull()) {
            serial.setRait(tmpElem.getAsString());
        }

        if ((tmpElem = jsonObject.get("title")) != null && !tmpElem.isJsonNull()) {
            serial.setTitle(tmpElem.getAsString());
        }

        if ((tmpElem = jsonObject.get("vk_post_id")) != null && !tmpElem.isJsonNull()) {
            serial.setVkPostId(tmpElem.getAsString());
        }

        JsonArray dArray = (JsonArray) jsonObject.get("description");
        if (dArray != null && !dArray.isJsonNull()) {
            final RealmList<Description> descriptions = new RealmList<>();
            for(int i = 0; i < dArray.size(); ++i) {
                descriptions.add(context.deserialize(dArray.get(i), Torrent.class));
            }
            serial.setDescription(descriptions);
        }

        if ((tmpElem = jsonObject.get("airdate")) != null && !tmpElem.isJsonNull()) {
            serial.setAirdate(tmpElem.getAsString());
        }

        if ((tmpElem = jsonObject.get("video_stream_url")) != null && !tmpElem.isJsonNull()) {
            serial.setVideoStreamUrl(tmpElem.getAsString());
        }

        realm.close();
        return serial;
    }

}
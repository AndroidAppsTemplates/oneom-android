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
import com.iam.oneom.core.entities.model.Poster;
import com.iam.oneom.core.entities.model.Serial;
import com.iam.oneom.core.entities.model.Status;

import java.lang.reflect.Type;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;

public class SerialDeserializer implements JsonDeserializer<Serial> {

    @Override
    public Serial deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Serial serial = new Serial();

        final JsonObject jsonObject = (JsonObject) jsonElement;

        JsonElement tmpElem;

        if ((tmpElem = jsonObject.get("id")) != null && !tmpElem.isJsonNull()) {
            serial.setId(tmpElem.getAsLong());
        }

        if ((tmpElem = jsonObject.get("poster")) != null && !tmpElem.isJsonNull()) {
            serial.setPoster(context.deserialize(tmpElem, Poster.class));
        }

        JsonArray oArray = (JsonArray) jsonObject.get("genre");
        if (oArray != null && !oArray.isJsonNull()) {
            final RealmList<Genre> genre = new RealmList<>();
            for (int i = 0; i < oArray.size(); ++i) {
                genre.add(context.deserialize(oArray.get(i), Genre.class));
            }
            serial.setGenre(genre);
        }

        JsonArray cArray = (JsonArray) jsonObject.get("country");
        if (cArray != null && !cArray.isJsonNull()) {
            final RealmList<Country> countries = new RealmList<>();
            for (int i = 0; i < cArray.size(); ++i) {
                countries.add(context.deserialize(cArray.get(i), Country.class));
            }
            serial.setCountry(countries);
        }

        JsonArray nArray = (JsonArray) jsonObject.get("network");
        if (nArray != null && !nArray.isJsonNull()) {
            final RealmList<Network> networks = new RealmList<>();
            for (int i = 0; i < nArray.size(); ++i) {
                networks.add(context.deserialize(nArray.get(i), Network.class));
            }
            serial.setNetwork(networks);
        }

        JsonArray eArray = (JsonArray) jsonObject.get("ep");
        if (eArray != null && !eArray.isJsonNull()) {
            final RealmList<Episode> episodes = new RealmList<>();
            for (int i = 0; i < eArray.size(); ++i) {
                episodes.add(context.deserialize(eArray.get(i), Episode.class));
            }
            serial.setEpisode(episodes);
        }

        JsonArray dArray = (JsonArray) jsonObject.get("description");
        if (dArray != null && !dArray.isJsonNull()) {
            final RealmList<Description> descriptions = new RealmList<>();
            for (int i = 0; i < dArray.size(); ++i) {
                descriptions.add(context.deserialize(dArray.get(i), Description.class));
            }
            serial.setDescription(descriptions);
        }

        if ((tmpElem = jsonObject.get("status")) != null && !tmpElem.isJsonNull()) {
            serial.setStatus(context.deserialize(tmpElem, Status.class));
        }

        if ((tmpElem = jsonObject.get("title")) != null && !tmpElem.isJsonNull()) {
            serial.setTitle(tmpElem.getAsString());
        }

        if ((tmpElem = jsonObject.get("title")) != null && !tmpElem.isJsonNull()) {
            serial.setTitle(tmpElem.getAsString());
        }

        if ((tmpElem = jsonObject.get("hide")) != null && !tmpElem.isJsonNull()) {
            serial.setHide(tmpElem.getAsInt());
        }

        if ((tmpElem = jsonObject.get("tvrage_id")) != null && !tmpElem.isJsonNull()) {
            serial.setTvrageId(tmpElem.getAsInt());
        }

        if ((tmpElem = jsonObject.get("tvmaze_id")) != null && !tmpElem.isJsonNull()) {
            serial.setTvmazeId(tmpElem.getAsInt());
        }

        if ((tmpElem = jsonObject.get("mdb_id")) != null && !tmpElem.isJsonNull()) {
            serial.setMdbId(tmpElem.getAsInt());
        }

        if ((tmpElem = jsonObject.get("tvdb_id")) != null && !tmpElem.isJsonNull()) {
            serial.setTvdbId(tmpElem.getAsInt());
        }

        if ((tmpElem = jsonObject.get("vk_group_id")) != null && !tmpElem.isJsonNull()) {
            serial.setVkGroupId(tmpElem.getAsInt());
        }

        if ((tmpElem = jsonObject.get("imdb_id")) != null && !tmpElem.isJsonNull()) {
            serial.setImbbId(tmpElem.getAsString());
        }

        if ((tmpElem = jsonObject.get("imdb_rating")) != null && !tmpElem.isJsonNull()) {
            serial.setImdbRating(tmpElem.getAsString());
        }

        if ((tmpElem = jsonObject.get("runtime")) != null && !tmpElem.isJsonNull()) {
            serial.setRuntime(tmpElem.getAsInt());
        }

        if ((tmpElem = jsonObject.get("start")) != null && !tmpElem.isJsonNull()) {
            serial.setStart(context.deserialize(tmpElem, Date.class));
        }

        if ((tmpElem = jsonObject.get("end")) != null && !tmpElem.isJsonNull()) {
            serial.setEnd(context.deserialize(tmpElem, Date.class));
        }

        if ((tmpElem = jsonObject.get("vk_group_update")) != null && !tmpElem.isJsonNull()) {
            serial.setVkGroupUpdate(tmpElem.getAsString());
        }

        if ((tmpElem = jsonObject.get("airtime")) != null && !tmpElem.isJsonNull()) {
            serial.setAirtime(tmpElem.getAsString());
        }

        if ((tmpElem = jsonObject.get("airdate")) != null && !tmpElem.isJsonNull()) {
            serial.setAirday(tmpElem.getAsString());
        }

        if ((tmpElem = jsonObject.get("timezone")) != null && !tmpElem.isJsonNull()) {
            serial.setTimezone(tmpElem.getAsString());
        }


        return serial;
    }

}
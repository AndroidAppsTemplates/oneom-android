package com.iam.oneom.core.entities.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.iam.oneom.core.entities.model.Country;
import com.iam.oneom.core.entities.model.Description;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.entities.model.Genre;
import com.iam.oneom.core.entities.model.Network;
import com.iam.oneom.core.entities.model.Online;
import com.iam.oneom.core.entities.model.Poster;
import com.iam.oneom.core.entities.model.Serial;
import com.iam.oneom.core.entities.model.Status;
import com.iam.oneom.core.entities.model.Subtitle;
import com.iam.oneom.core.entities.model.Torrent;

import java.lang.reflect.Type;
import java.util.Date;

import io.realm.RealmList;

public class SerialSerializer implements JsonSerializer<Serial> {

    @Override
    public JsonElement serialize(Serial serial, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", serial.getId());
        jsonObject.addProperty("poster_id", serial.getPoster() == null ? 0 : serial.getPoster().getId());
        if (serial.getGenre() != null) {
            final JsonArray jsonArray = new JsonArray();
            for (final Genre genre : serial.getGenre()) {
                jsonArray.add(context.serialize(genre));
            }
            jsonObject.add("genre", jsonArray);
        }
        if (serial.getCountry() != null) {
            final JsonArray jsonArray = new JsonArray();
            for (final Country country : serial.getCountry()) {
                jsonArray.add(context.serialize(country));
            }
            jsonObject.add("country", jsonArray);
        }
        if (serial.getNetwork() != null) {
            final JsonArray jsonArray = new JsonArray();
            for (final Network network : serial.getNetwork()) {
                jsonArray.add(context.serialize(network));
            }
            jsonObject.add("network", jsonArray);
        }
        if (serial.getEpisode() != null) {
            final JsonArray jsonArray = new JsonArray();
            for (final Episode episode : serial.getEpisode()) {
                jsonArray.add(context.serialize(episode));
            }
            jsonObject.add("ep", jsonArray);
        }
        if (serial.getDescription() != null) {
            final JsonArray jsonArray = new JsonArray();
            for (final Description description : serial.getDescription()) {
                jsonArray.add(context.serialize(description));
            }
            jsonObject.add("description", jsonArray);
        }

        jsonObject.addProperty("status_id", serial.getStatus() == null ? 0 : serial.getStatus().getId());

//    private String airtime;
//    private String airday;
//    private String timezone;
//    private String vkGroupUpdate;


        jsonObject.addProperty("title", serial.getTitle());
        jsonObject.addProperty("hide", serial.getHide());
        jsonObject.addProperty("tvrage_id", serial.getTvrageId());
        jsonObject.addProperty("tvmaze_id", serial.getTvmazeId());
        jsonObject.addProperty("mdb_id", serial.getMdbId());
        jsonObject.addProperty("tvdb_id", serial.getTvdbId());
        jsonObject.addProperty("vk_group_id", serial.getVkGroupId());
        jsonObject.addProperty("imdb_id", serial.getImbbId());
        jsonObject.addProperty("imdb_rating", serial.getImdbRating());
        jsonObject.addProperty("runtime", serial.getRuntime());
        jsonObject.add("start", context.serialize(serial.getStart()));
        jsonObject.add("end", context.serialize(serial.getEnd()));
        jsonObject.addProperty("airtime", serial.getAirtime());
        jsonObject.addProperty("airday", serial.getAirday());
        jsonObject.addProperty("timezone", serial.getTimezone());
        jsonObject.addProperty("vk_group_update", serial.getVkGroupUpdate());

        return jsonObject;
    }
}
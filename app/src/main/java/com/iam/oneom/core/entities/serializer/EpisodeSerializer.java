package com.iam.oneom.core.entities.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.iam.oneom.core.entities.model.Description;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.entities.model.Lang;
import com.iam.oneom.core.entities.model.Online;
import com.iam.oneom.core.entities.model.Serial;
import com.iam.oneom.core.entities.model.Subtitle;
import com.iam.oneom.core.entities.model.Torrent;

import java.lang.reflect.Type;

import io.realm.RealmList;

public class EpisodeSerializer implements JsonSerializer<Episode> {

    @Override
    public JsonElement serialize(Episode episode, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", episode.getId());
        if (episode.getSerial() != null) {
            jsonObject.add("serial", context.serialize(episode.getSerial()));
        }
        jsonObject.addProperty("ep", episode.getEp());
        jsonObject.addProperty("season", episode.getSeason());
        jsonObject.addProperty("rait", episode.getRait());
        jsonObject.addProperty("title", episode.getTitle());
        jsonObject.addProperty("vk_post_id", episode.getVkPostId());
        jsonObject.addProperty("airdate", episode.getAirdate());
        jsonObject.addProperty("video_stream_url", episode.getVideoStreamUrl());

        if (episode.getTorrent() != null) {
            final JsonArray jsonArray = new JsonArray();
            for (final Torrent torrent : episode.getTorrent()) {
                jsonArray.add(context.serialize(torrent));
            }
            jsonObject.add("torrent", jsonArray);
        }

        if (episode.getOnline() != null) {
            final JsonArray jsonArray = new JsonArray();
            for (final Online online : episode.getOnline()) {
                jsonArray.add(context.serialize(online));
            }
            jsonObject.add("online", jsonArray);
        }

        if (episode.getSubtitle() != null) {
            final JsonArray jsonArray = new JsonArray();
            for (final Subtitle subtitle : episode.getSubtitle()) {
                jsonArray.add(context.serialize(subtitle));
            }
            jsonObject.add("subtitle", jsonArray);
        }

        if (episode.getDescription() != null) {
            final JsonArray jsonArray = new JsonArray();
            for (final Description description : episode.getDescription()) {
                jsonArray.add(context.serialize(description));
            }
            jsonObject.add("description", jsonArray);
        }

        return jsonObject;
    }
}
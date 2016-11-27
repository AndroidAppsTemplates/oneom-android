package com.iam.oneom.core.entities.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.iam.oneom.core.entities.model.Lang;
import com.iam.oneom.core.entities.model.Quality;
import com.iam.oneom.core.entities.model.Source;
import com.iam.oneom.core.entities.model.Subtitle;
import com.iam.oneom.core.entities.model.Torrent;

import java.lang.reflect.Type;

public class TorrentSerializer implements JsonSerializer<Torrent> {

    @Override
    public JsonElement serialize(Torrent torrent, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", torrent.getId());
        jsonObject.addProperty("title", torrent.getTitle());
        jsonObject.addProperty("url", torrent.getUrl());
        jsonObject.addProperty("lang_id", torrent.getLang() == null ? 0 : torrent.getLang().getId());
        jsonObject.addProperty("source_id", torrent.getSource() == null ? 0 : torrent.getSource().getId());
        jsonObject.addProperty("quality_id", torrent.getQuality() == null ? 0 : torrent.getQuality().getId());
        jsonObject.addProperty("vk_post_id", torrent.getVkPostId());
        jsonObject.addProperty("value", torrent.getValue());

        return jsonObject;
    }
}
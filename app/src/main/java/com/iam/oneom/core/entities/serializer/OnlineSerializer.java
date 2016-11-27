package com.iam.oneom.core.entities.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.iam.oneom.core.entities.model.Lang;
import com.iam.oneom.core.entities.model.Network;
import com.iam.oneom.core.entities.model.Online;
import com.iam.oneom.core.entities.model.Quality;
import com.iam.oneom.core.entities.model.Source;

import java.lang.reflect.Type;

public class OnlineSerializer implements JsonSerializer<Online> {

//    private long id;
//    private Source source;
//    private Lang lang;
//    private Quality quality;
//    private String title;
//    private String embedCode;
//    private String url;
//    private String videoUrl;
//    private String posterUrl;


    @Override
    public JsonElement serialize(Online online, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", online.getId());
        jsonObject.addProperty("source_id", online.getSource() == null ? 0 : online.getSource().getId());
        jsonObject.addProperty("lang_id", online.getLang() == null ? 0 : online.getLang().getId());
        jsonObject.addProperty("quality_id", online.getQuality() == null ? 0 : online.getQuality().getId());
        jsonObject.addProperty("title", online.getTitle());
        jsonObject.addProperty("embed_code", online.getEmbedCode());
        jsonObject.addProperty("url", online.getUrl());
        jsonObject.addProperty("video_url", online.getVideoUrl());
        jsonObject.addProperty("poster_url", online.getPosterUrl());

        return jsonObject;
    }
}
package com.iam.oneom.core.entities.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.iam.oneom.core.entities.model.Lang;
import com.iam.oneom.core.entities.model.Source;
import com.iam.oneom.core.entities.model.Status;
import com.iam.oneom.core.entities.model.Subtitle;

import java.lang.reflect.Type;

public class SubtitleSerializer implements JsonSerializer<Subtitle> {

//    private long id;
//    private String title;
//    private String url;
//    private Lang lang;
//    private Source source;
//    private int fileId;
//    private int assocId;
//    private String assocType;

    @Override
    public JsonElement serialize(Subtitle subtitle, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", subtitle.getId());
        jsonObject.addProperty("title", subtitle.getTitle());
        jsonObject.addProperty("url", subtitle.getUrl());
        jsonObject.addProperty("lang_id", subtitle.getLang() == null ? 0 : subtitle.getLang().getId());
        jsonObject.addProperty("source_id", subtitle.getSource() == null ? 0 : subtitle.getSource().getId());
        jsonObject.addProperty("file_id", subtitle.getFileId());
        jsonObject.addProperty("assoc_id", subtitle.getAssocId());
        jsonObject.addProperty("assoc_type", subtitle.getAssocType());

        return jsonObject;
    }
}
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
import com.iam.oneom.core.entities.model.Serial;
import com.iam.oneom.core.entities.model.Source;

import java.lang.reflect.Type;

public class SourceSerializer implements JsonSerializer<Source> {

//    private long id;
//    private int typeId; // TODO implement into deserializer
//    private int active;
//    private String searchPage;
//    private String search;
//    private String searchStep;
//    private String name;
//    private String data;
//    private int login;
//    private String url;

    @Override
    public JsonElement serialize(Source source, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", source.getId());

        jsonObject.addProperty("type_id", source.getTypeId());
        jsonObject.addProperty("active", source.getActive());
        jsonObject.addProperty("search_page", source.getSearchPage());
        jsonObject.addProperty("search", source.getSearch());
        jsonObject.addProperty("search_step", source.getSearchStep());
        jsonObject.addProperty("name", source.getName());
        jsonObject.addProperty("data", source.getData());
        jsonObject.addProperty("login", source.getLogin());
        jsonObject.addProperty("url", source.getUrl());

        return jsonObject;
    }
}
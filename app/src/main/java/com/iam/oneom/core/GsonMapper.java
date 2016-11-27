package com.iam.oneom.core;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iam.oneom.core.entities.deserializer.CountryDeserializer;
import com.iam.oneom.core.entities.deserializer.DataConfigRequestDeserializer;
import com.iam.oneom.core.entities.deserializer.DescriptionDeserializer;
import com.iam.oneom.core.entities.deserializer.EpisodeDeserializer;
import com.iam.oneom.core.entities.deserializer.FileDeserializer;
import com.iam.oneom.core.entities.deserializer.GenreDeserializer;
import com.iam.oneom.core.entities.deserializer.LangDeserializer;
import com.iam.oneom.core.entities.deserializer.NetworkDeserializer;
import com.iam.oneom.core.entities.deserializer.OnlineDeserializer;
import com.iam.oneom.core.entities.deserializer.PosterDeserializer;
import com.iam.oneom.core.entities.deserializer.QualityDeserializer;
import com.iam.oneom.core.entities.deserializer.QualityGroupDeserializer;
import com.iam.oneom.core.entities.deserializer.SerialDeserializer;
import com.iam.oneom.core.entities.deserializer.SourceDeserializer;
import com.iam.oneom.core.entities.deserializer.SubtitleDeserializer;
import com.iam.oneom.core.entities.deserializer.TorrentDeserializer;
import com.iam.oneom.core.entities.model.Country;
import com.iam.oneom.core.entities.model.Description;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.entities.model.File;
import com.iam.oneom.core.entities.model.Genre;
import com.iam.oneom.core.entities.model.Lang;
import com.iam.oneom.core.entities.model.Network;
import com.iam.oneom.core.entities.model.Online;
import com.iam.oneom.core.entities.model.Poster;
import com.iam.oneom.core.entities.model.Quality;
import com.iam.oneom.core.entities.model.QualityGroup;
import com.iam.oneom.core.entities.model.Serial;
import com.iam.oneom.core.entities.model.Source;
import com.iam.oneom.core.entities.model.Status;
import com.iam.oneom.core.entities.model.Subtitle;
import com.iam.oneom.core.entities.model.Torrent;
import com.iam.oneom.core.entities.serializer.CountrySerializer;
import com.iam.oneom.core.entities.serializer.DescriptionSerializer;
import com.iam.oneom.core.entities.serializer.EpisodeSerializer;
import com.iam.oneom.core.entities.serializer.FileSerializer;
import com.iam.oneom.core.entities.serializer.GenreSerializer;
import com.iam.oneom.core.entities.serializer.LangSerializer;
import com.iam.oneom.core.entities.serializer.NetworkSerializer;
import com.iam.oneom.core.entities.serializer.OnlineSerializer;
import com.iam.oneom.core.entities.serializer.PosterSerializer;
import com.iam.oneom.core.entities.serializer.QualityGroupSerializer;
import com.iam.oneom.core.entities.serializer.QualitySerializer;
import com.iam.oneom.core.entities.serializer.SerialSerializer;
import com.iam.oneom.core.entities.serializer.SourceSerializer;
import com.iam.oneom.core.entities.serializer.StatusSerializer;
import com.iam.oneom.core.entities.serializer.SubtitleSerializer;
import com.iam.oneom.core.entities.serializer.TorrentSerializer;
import com.iam.oneom.core.network.request.DataConfigRequest;

import org.json.JSONException;
import org.json.JSONObject;

public enum GsonMapper {

    INSTANCE;

    public final Gson gson;

    GsonMapper() {
        try {
            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd")
                    .registerTypeAdapter(Class.forName(Country.class.getCanonicalName()), new CountrySerializer())
                    .registerTypeAdapter(Class.forName(Country.class.getCanonicalName()), new CountryDeserializer())
                    .registerTypeAdapter(Class.forName(DataConfigRequest.class.getCanonicalName()), new DataConfigRequestDeserializer())
                    .registerTypeAdapter(Class.forName(Description.class.getCanonicalName()), new DescriptionDeserializer())
                    .registerTypeAdapter(Class.forName(Description.class.getCanonicalName()), new DescriptionSerializer())
                    .registerTypeAdapter(Class.forName(Episode.class.getCanonicalName()), new EpisodeDeserializer())
                    .registerTypeAdapter(Class.forName(Episode.class.getCanonicalName()), new EpisodeSerializer())
                    .registerTypeAdapter(Class.forName(File.class.getCanonicalName()), new FileDeserializer())
                    .registerTypeAdapter(Class.forName(File.class.getCanonicalName()), new FileSerializer())
                    .registerTypeAdapter(Class.forName(Genre.class.getCanonicalName()), new GenreDeserializer())
                    .registerTypeAdapter(Class.forName(Genre.class.getCanonicalName()), new GenreSerializer())
                    .registerTypeAdapter(Class.forName(Lang.class.getCanonicalName()), new LangDeserializer())
                    .registerTypeAdapter(Class.forName(Lang.class.getCanonicalName()), new LangSerializer())
                    .registerTypeAdapter(Class.forName(Network.class.getCanonicalName()), new NetworkDeserializer())
                    .registerTypeAdapter(Class.forName(Network.class.getCanonicalName()), new NetworkSerializer())
                    .registerTypeAdapter(Class.forName(Online.class.getCanonicalName()), new OnlineDeserializer())
                    .registerTypeAdapter(Class.forName(Online.class.getCanonicalName()), new OnlineSerializer())
                    .registerTypeAdapter(Class.forName(Poster.class.getCanonicalName()), new PosterDeserializer())
                    .registerTypeAdapter(Class.forName(Poster.class.getCanonicalName()), new PosterSerializer())
                    .registerTypeAdapter(Class.forName(Quality.class.getCanonicalName()), new QualityDeserializer())
                    .registerTypeAdapter(Class.forName(Quality.class.getCanonicalName()), new QualitySerializer())
                    .registerTypeAdapter(Class.forName(QualityGroup.class.getCanonicalName()), new QualityGroupDeserializer())
                    .registerTypeAdapter(Class.forName(QualityGroup.class.getCanonicalName()), new QualityGroupSerializer())
                    .registerTypeAdapter(Class.forName(Serial.class.getCanonicalName()), new SerialDeserializer())
                    .registerTypeAdapter(Class.forName(Serial.class.getCanonicalName()), new SerialSerializer())
                    .registerTypeAdapter(Class.forName(Source.class.getCanonicalName()), new SourceDeserializer())
                    .registerTypeAdapter(Class.forName(Source.class.getCanonicalName()), new SourceSerializer())
                    .registerTypeAdapter(Class.forName(Status.class.getCanonicalName()), new StatusSerializer())
                    .registerTypeAdapter(Class.forName(Status.class.getCanonicalName()), new StatusSerializer())
                    .registerTypeAdapter(Class.forName(Subtitle.class.getCanonicalName()), new SubtitleDeserializer())
                    .registerTypeAdapter(Class.forName(Subtitle.class.getCanonicalName()), new SubtitleSerializer())
                    .registerTypeAdapter(Class.forName(Torrent.class.getCanonicalName()), new TorrentDeserializer())
                    .registerTypeAdapter(Class.forName(Torrent.class.getCanonicalName()), new TorrentSerializer())
                    .create();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public JSONObject toJson(Object object) {
        try {
            return new JSONObject(gson.toJson(object));
        } catch (JSONException pE) {
            pE.printStackTrace();
        }
        return null;
    }

    public <T> T fromJson(final @Nullable String jString, Class<T> classOfT) {
        if (jString == null) {
            return null;
        }
        return gson.fromJson(jString, classOfT);
    }

    public <T> T fromJson(final @Nullable JSONObject jsonObject, Class<T> classOfT) {
        if (jsonObject == null) {
            return null;
        }
        return gson.fromJson(jsonObject.toString(), classOfT);
    }
}
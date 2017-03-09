package com.iam.oneom.core.entities.deserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.iam.oneom.core.entities.model.Country;
import com.iam.oneom.core.entities.model.Genre;
import com.iam.oneom.core.entities.model.Lang;
import com.iam.oneom.core.entities.model.Network;
import com.iam.oneom.core.entities.model.Quality;
import com.iam.oneom.core.entities.model.QualityGroup;
import com.iam.oneom.core.entities.model.Source;
import com.iam.oneom.core.entities.model.Status;
import com.iam.oneom.core.network.response.DataConfigResponse;

import java.lang.reflect.Type;

import io.realm.RealmList;

public class DataConfigResponseDeserializer implements JsonDeserializer<DataConfigResponse> {

    @Override
    public DataConfigResponse deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        DataConfigResponse dataConfigRequest = new DataConfigResponse();

        final JsonObject jsonObject = (JsonObject) jsonElement;

        JsonArray cArray = (JsonArray) jsonObject.get("country");
        if (cArray != null && !cArray.isJsonNull()) {
            final RealmList<Country> countries = new RealmList<>();
            for (int i = 0; i < cArray.size(); ++i) {
                countries.add(context.deserialize(cArray.get(i), Country.class));
            }
            dataConfigRequest.setCountries(countries);
        }

        JsonArray lArray = (JsonArray) jsonObject.get("lang");
        if (lArray != null && !lArray.isJsonNull()) {
            final RealmList<Lang> langs = new RealmList<>();
            for (int i = 0; i < lArray.size(); ++i) {
                langs.add(context.deserialize(lArray.get(i), Lang.class));
            }
            dataConfigRequest.setLang(langs);
        }

        JsonArray qgArray = (JsonArray) jsonObject.get("gquality");
        if (qgArray != null && !qgArray.isJsonNull()) {
            final RealmList<QualityGroup> qualityGroups = new RealmList<>();
            for (int i = 0; i < qgArray.size(); ++i) {
                qualityGroups.add(context.deserialize(qgArray.get(i), QualityGroup.class));
            }
            dataConfigRequest.setQualityGroups(qualityGroups);
        }

        JsonArray qArray = (JsonArray) jsonObject.get("quality");
        if (qArray != null && !qArray.isJsonNull()) {
            final RealmList<Quality> qualities = new RealmList<>();
            for (int i = 0; i < qArray.size(); ++i) {
                qualities.add(context.deserialize(qArray.get(i), Quality.class));
            }
            dataConfigRequest.setQualities(qualities);
        }

        JsonArray gArray = (JsonArray) jsonObject.get("genre");
        if (gArray != null && !gArray.isJsonNull()) {
            final RealmList<Genre> genres = new RealmList<>();
            for (int i = 0; i < gArray.size(); ++i) {
                genres.add(context.deserialize(gArray.get(i), Genre.class));
            }
            dataConfigRequest.setGenres(genres);
        }

        JsonArray sArray = (JsonArray) jsonObject.get("source");
        if (sArray != null && !sArray.isJsonNull()) {
            final RealmList<Source> sources = new RealmList<>();
            for (int i = 0; i < sArray.size(); ++i) {
                sources.add(context.deserialize(sArray.get(i), Source.class));
            }
            dataConfigRequest.setSources(sources);
        }

        JsonArray stArray = (JsonArray) jsonObject.get("status");
        if (stArray != null && !stArray.isJsonNull()) {
            final RealmList<Status> statuses = new RealmList<>();
            for (int i = 0; i < stArray.size(); ++i) {
                statuses.add(context.deserialize(stArray.get(i), Status.class));
            }
            dataConfigRequest.setStatuses(statuses);
        }

        JsonArray nArray = (JsonArray) jsonObject.get("network");
        if (nArray != null && !nArray.isJsonNull()) {
            final RealmList<Network> networks = new RealmList<>();
            for (int i = 0; i < nArray.size(); ++i) {
                networks.add(context.deserialize(nArray.get(i), Network.class));
            }
            dataConfigRequest.setNetworks(networks);
        }

        return dataConfigRequest;
    }

}
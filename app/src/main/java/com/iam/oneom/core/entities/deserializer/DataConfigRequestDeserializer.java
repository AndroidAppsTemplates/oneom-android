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
import com.iam.oneom.core.entities.model.Quality;
import com.iam.oneom.core.entities.model.QualityGroup;
import com.iam.oneom.core.entities.model.Serial;
import com.iam.oneom.core.entities.model.Source;
import com.iam.oneom.core.entities.model.Status;
import com.iam.oneom.core.network.request.DataConfigRequest;

import java.lang.reflect.Type;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;

public class DataConfigRequestDeserializer implements JsonDeserializer<DataConfigRequest> {

    @Override
    public DataConfigRequest deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        DataConfigRequest dataConfigRequest = new DataConfigRequest();
        Realm realm = Realm.getDefaultInstance();

        final JsonObject jsonObject = (JsonObject) jsonElement;

        JsonArray cArray = (JsonArray) jsonObject.get("country");
        if (cArray != null && !cArray.isJsonNull()) {
            final RealmList<Country> countries = new RealmList<>();
            for (int i = 0; i < cArray.size(); ++i) {
                countries.add(context.deserialize(cArray.get(i), Country.class));
            }
            dataConfigRequest.setCountries(countries);
            realm.executeTransaction(r -> r.insertOrUpdate(countries));
        }

        JsonArray qgArray = (JsonArray) jsonObject.get("gquality");
        if (qgArray != null && !qgArray.isJsonNull()) {
            final RealmList<QualityGroup> qualityGroups = new RealmList<>();
            for (int i = 0; i < qgArray.size(); ++i) {
                qualityGroups.add(context.deserialize(qgArray.get(i), QualityGroup.class));
            }
            dataConfigRequest.setQualityGroups(qualityGroups);
            realm.executeTransaction(r -> r.insertOrUpdate(qualityGroups));
        }

        JsonArray qArray = (JsonArray) jsonObject.get("quality");
        if (qArray != null && !qArray.isJsonNull()) {
            final RealmList<Quality> qualities = new RealmList<>();
            for (int i = 0; i < qArray.size(); ++i) {
                qualities.add(context.deserialize(qArray.get(i), Quality.class));
            }
            dataConfigRequest.setQualities(qualities);
            realm.executeTransaction(r -> r.insertOrUpdate(qualities));
        }

        JsonArray gArray = (JsonArray) jsonObject.get("genre");
        if (gArray != null && !gArray.isJsonNull()) {
            final RealmList<Genre> genres = new RealmList<>();
            for (int i = 0; i < gArray.size(); ++i) {
                genres.add(context.deserialize(gArray.get(i), Genre.class));
            }
            dataConfigRequest.setGenres(genres);
            realm.executeTransaction(r -> r.insertOrUpdate(genres));
        }

        JsonArray sArray = (JsonArray) jsonObject.get("source");
        if (sArray != null && !sArray.isJsonNull()) {
            final RealmList<Source> sources = new RealmList<>();
            for (int i = 0; i < sArray.size(); ++i) {
                sources.add(context.deserialize(sArray.get(i), Source.class));
            }
            dataConfigRequest.setSources(sources);
            realm.executeTransaction(r -> r.insertOrUpdate(sources));
        }

        JsonArray stArray = (JsonArray) jsonObject.get("status");
        if (stArray != null && !stArray.isJsonNull()) {
            final RealmList<Status> statuses = new RealmList<>();
            for (int i = 0; i < stArray.size(); ++i) {
                statuses.add(context.deserialize(stArray.get(i), Status.class));
            }
            dataConfigRequest.setStatuses(statuses);
            realm.executeTransaction(r -> r.insertOrUpdate(statuses));
        }

        JsonArray nArray = (JsonArray) jsonObject.get("network");
        if (nArray != null && !nArray.isJsonNull()) {
            final RealmList<Network> networks = new RealmList<>();
            for (int i = 0; i < nArray.size(); ++i) {
                networks.add(context.deserialize(nArray.get(i), Country.class));
            }
            dataConfigRequest.setNetworks(networks);
            realm.executeTransaction(r -> r.insertOrUpdate(networks));
        }

        realm.close();
        return dataConfigRequest;
    }

}
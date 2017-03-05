package com.iam.oneom.core.network.response;


import com.iam.oneom.core.entities.model.Country;
import com.iam.oneom.core.entities.model.Genre;
import com.iam.oneom.core.entities.model.Lang;
import com.iam.oneom.core.entities.model.Network;
import com.iam.oneom.core.entities.model.Quality;
import com.iam.oneom.core.entities.model.QualityGroup;
import com.iam.oneom.core.entities.model.Source;
import com.iam.oneom.core.entities.model.Status;

import io.realm.RealmList;
import io.realm.RealmObject;

public class DataConfigResponse extends RealmObject {

    RealmList<Lang> lang;
    RealmList<Quality> qualities;
    RealmList<QualityGroup> qualityGroups;
    RealmList<Genre> genres;
    RealmList<Source> sources;
    RealmList<Country> countries;
    RealmList<Network> networks;
    RealmList<Status> statuses;

    public RealmList<Lang> getLang() {
        return lang;
    }

    public void setLang(RealmList<Lang> lang) {
        this.lang = lang;
    }

    public RealmList<Quality> getQualities() {
        return qualities;
    }

    public void setQualities(RealmList<Quality> qualities) {
        this.qualities = qualities;
    }

    public RealmList<QualityGroup> getQualityGroups() {
        return qualityGroups;
    }

    public void setQualityGroups(RealmList<QualityGroup> qualityGroups) {
        this.qualityGroups = qualityGroups;
    }

    public RealmList<Genre> getGenres() {
        return genres;
    }

    public void setGenres(RealmList<Genre> genres) {
        this.genres = genres;
    }

    public RealmList<Source> getSources() {
        return sources;
    }

    public void setSources(RealmList<Source> sources) {
        this.sources = sources;
    }

    public RealmList<Country> getCountries() {
        return countries;
    }

    public void setCountries(RealmList<Country> countries) {
        this.countries = countries;
    }

    public RealmList<Network> getNetworks() {
        return networks;
    }

    public void setNetworks(RealmList<Network> networks) {
        this.networks = networks;
    }

    public RealmList<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(RealmList<Status> statuses) {
        this.statuses = statuses;
    }
}

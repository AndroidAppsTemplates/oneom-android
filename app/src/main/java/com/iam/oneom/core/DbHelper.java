package com.iam.oneom.core;

import android.content.Context;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmQuery;

public class DbHelper {

    private static final long SCHEMA_VERSION = 11;

    static Realm getRealm() {
        return Realm.getDefaultInstance();
    }

    public static void init(Context context) {
        Realm.init(context);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
                .schemaVersion(SCHEMA_VERSION)
                .name("main_db.realm")
                .deleteRealmIfMigrationNeeded()
                .build());
    }

    public static <T extends RealmObject> RealmQuery<T> where(Class<T> tClass) {
        Realm localInstance = getRealm();
        return localInstance.where(tClass);
    }

    public static void insert(RealmObject object) {
        insert(object, null);
    }

    public static void insert(RealmObject object, OnInsertionFinishListener l) {
        getRealm().executeTransactionAsync(
                r ->
                        r.insertOrUpdate(object),
                () -> {
                    if (l != null) {
                        l.onFinish();
                    }
                });

    }

    public static void insertAll(List<? extends RealmObject> objects) {
        insertAll(objects, null);
    }

    public static void insertAll(List<? extends RealmObject> objects, OnInsertionFinishListener l) {
        Realm localInstance = getRealm();
        localInstance.beginTransaction();
        localInstance.insertOrUpdate(objects);
        localInstance.commitTransaction();
        if (l != null) {
            l.onFinish();
        }
    }

    public static <T extends RealmObject> T clone(T object) {
        if (object == null) {
            return null;
        }
        if (!(RealmObject.isManaged(object) && RealmObject.isValid(object))) {
            return object;
        }

        return getRealm().copyFromRealm(object);
    }

    public interface OnInsertionFinishListener {
        void onFinish();
    }
}

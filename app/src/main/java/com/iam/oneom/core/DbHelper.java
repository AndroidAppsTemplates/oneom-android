package com.iam.oneom.core;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;

public class DbHelper {

    private static DbHelper instance;

    Realm realm;

    private DbHelper() {
        realm = Realm.getDefaultInstance();
    }

    private static Realm getInstance() {
        if (instance == null) {
            instance = new DbHelper();
        }

        return instance.getRealm();
    }

    public static <T extends RealmObject> RealmQuery<T> where(Class<T> tClass) {
        Realm localInstance = getInstance();
        return localInstance.where(tClass);
    }

    public static void insert(RealmObject object) {
        insert(object, null);
    }

    public static void insert(RealmObject object, OnInsertionFinishListener l) {
        getInstance().executeTransactionAsync(
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
        Realm localInstance = getInstance();
        localInstance.beginTransaction();
        localInstance.insertOrUpdate(objects);
        localInstance.commitTransaction();
        if (l != null) {
            l.onFinish();
        }
    }

    public static final <T extends RealmObject> T clone(T object) {
        if (object == null) {
            return object;
        }
        if (!(RealmObject.isManaged(object) && RealmObject.isValid(object))) {
            return object;
        }

        return getInstance().copyFromRealm(object);
    }

    private Realm getRealm() {
        return realm;
    }

    public interface OnInsertionFinishListener {
        void onFinish();
    }
}

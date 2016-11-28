package com.iam.oneom.core;

import com.iam.oneom.core.entities.model.Status;

import io.realm.Realm;

public class DBHelper {

    private static DBHelper instance;

    Realm realm;

    private DBHelper() {
        realm = Realm.getDefaultInstance();
    }

    public static DBHelper getInstance() {
        if (instance == null) {
            instance = new DBHelper();
        }

        return instance;
    }

    public Realm getRealm() {
        return realm;
    }
}

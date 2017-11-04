package com.dev.notes;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class NotesApplication extends Application {
    @Override
    public void onCreate() {

        super.onCreate();
        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder().name("notesrealm.realm").build();
        Realm.setDefaultConfiguration(config);
    }

}

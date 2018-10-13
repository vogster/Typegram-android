package com.unlogicon.typegram;


import android.app.Application;
import android.arch.persistence.room.Room;

import com.unlogicon.typegram.abstracts.AppDatabase;
import com.unlogicon.typegram.di.Components;

public class TgramApplication extends Application {

    public static TgramApplication instance;

    private Components components;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        components = new Components(getApplicationContext());
        components.getAppComponent().inject(this);

    }

    public static TgramApplication getInstance() {
        return instance;
    }

    public Components getComponents() {
        return components;
    }
}

package com.unlogicon.typegram;


import android.app.Application;

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

package com.unlogicon.typegram.di;

import android.content.Context;

import com.unlogicon.typegram.di.components.AppComponent;
import com.unlogicon.typegram.di.components.DaggerAppComponent;
import com.unlogicon.typegram.di.modules.AppModule;
import com.unlogicon.typegram.di.modules.RoomModule;

/**
 * Nikita Korovkin 11.10.2018.
 */
public class Components {
    private AppComponent appComponent;

    public Components(Context context) {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(context))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}

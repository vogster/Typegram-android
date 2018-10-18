package com.unlogicon.typegram.di;

import android.content.Context;

import com.unlogicon.typegram.di.components.AppComponent;
import com.unlogicon.typegram.di.components.DaggerAppComponent;
import com.unlogicon.typegram.di.components.DaggerEditorComponent;
import com.unlogicon.typegram.di.components.EditorComponent;
import com.unlogicon.typegram.di.modules.AppModule;

/**
 * Nikita Korovkin 11.10.2018.
 */
public class Components {

    private AppComponent appComponent;
    private EditorComponent editorComponent;

    public Components(Context context) {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(context))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public EditorComponent getEditorComponent(){
        if(editorComponent == null) {
            editorComponent = DaggerEditorComponent.builder()
                    .appComponent(appComponent)
                    .build();
        }
        return editorComponent;
    }

    public void removeEditorComponent(){
        editorComponent = null;
    }

}

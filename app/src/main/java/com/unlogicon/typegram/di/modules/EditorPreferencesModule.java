package com.unlogicon.typegram.di.modules;

import android.content.Context;

import com.unlogicon.typegram.di.scopes.EditorScope;
import com.unlogicon.typegram.utils.EditorPreferencesUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Nikita Korovkin 18.10.2018.
 */

@Module
public class EditorPreferencesModule {

    @Provides
    @EditorScope
    EditorPreferencesUtils providesEditorPreferencesUtils(Context context){
        return new EditorPreferencesUtils(context);
    }

}

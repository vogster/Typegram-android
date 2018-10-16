package com.unlogicon.typegram.di.modules;

import android.content.Context;

import com.unlogicon.typegram.utils.SharedPreferencesUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Nikita Korovkin 16.10.2018.
 */

@Module
public class SharedPreferencesModule {

    @Provides
    @Singleton
    SharedPreferencesUtils providesSharedPreferencesUtils(Context context){
        return new SharedPreferencesUtils(context);
    }
}

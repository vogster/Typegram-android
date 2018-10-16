package com.unlogicon.typegram.di.modules;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.unlogicon.typegram.abstracts.AppDatabase;
import com.unlogicon.typegram.interfaces.dao.ArticlesDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Nikita Korovkin 11.10.2018.
 */
@Module
public class RoomModule {

    @Singleton
    @Provides
    AppDatabase providesRoomDatabase(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "database").build();
    }

    @Singleton
    @Provides
    ArticlesDao providesArticlesDao(AppDatabase database) {
        return database.getArticlesDao();
    }

}

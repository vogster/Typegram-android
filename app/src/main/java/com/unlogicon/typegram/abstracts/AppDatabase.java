package com.unlogicon.typegram.abstracts;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.unlogicon.typegram.interfaces.dao.ArticlesDao;
import com.unlogicon.typegram.models.Article;

/**
 * Nikita Korovkin 08.10.2018.
 */
@Database(entities = {Article.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ArticlesDao getArticlesDao();
}

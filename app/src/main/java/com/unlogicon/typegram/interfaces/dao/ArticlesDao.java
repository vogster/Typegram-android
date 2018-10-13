package com.unlogicon.typegram.interfaces.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.unlogicon.typegram.models.Article;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Nikita Korovkin 08.10.2018.
 */
@Dao
public interface ArticlesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Article... article);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Article> articles);

    @Delete
    void delete(Article article);

    @Query("SELECT * FROM article")
    Flowable<List<Article>> getAllArticles();

    @Query("SELECT * FROM article ORDER BY id DESC")
    Flowable<List<Article>> getAllArticlesByDesc();

    @Query("SELECT * FROM article ORDER BY id DESC LIMIT :limit OFFSET :offset")
    Flowable<List<Article>> getArticlesByDescWithLimit(int limit, int offset);

    @Query("SELECT * FROM article ORDER BY id DESC LIMIT :limit")
    Flowable<List<Article>> getArticlesByDescWithLimit(int limit);

    @Query("SELECT * FROM article ORDER BY id DESC LIMIT :limit OFFSET :offset")
    Single<List<Article>> getArticlesByDescWithLimitSingle(int limit, int offset);
}

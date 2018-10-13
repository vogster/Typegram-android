package com.unlogicon.typegram.presenters.activities;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.firebase.messaging.FirebaseMessaging;
import com.unlogicon.typegram.Constants;
import com.unlogicon.typegram.TgramApplication;
import com.unlogicon.typegram.abstracts.AppDatabase;
import com.unlogicon.typegram.adapters.MainRecyclerViewAdapter;
import com.unlogicon.typegram.interfaces.activities.MainActivityView;
import com.unlogicon.typegram.interfaces.api.RestApi;
import com.unlogicon.typegram.interfaces.dao.ArticlesDao;
import com.unlogicon.typegram.models.Article;
import com.unlogicon.typegram.ui.activities.MainActivity;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class MainActivityPresenter extends MvpPresenter<MainActivityView> {

    private MainRecyclerViewAdapter adapter;

    private int dbOffset = 0;
    private int lastID = 0;


    List<Article> articlesList;

    @Inject
    RestApi restApi;

    @Inject
    ArticlesDao articlesDao;

    public MainActivityPresenter() {
        TgramApplication.getInstance().getComponents().getAppComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        articlesList = new ArrayList<>();
        adapter = new MainRecyclerViewAdapter(articlesList);
        getViewState().setAdapter(adapter);

        //TODO This Shit
        articlesDao.getAllArticlesByDesc()
                .observeOn(AndroidSchedulers.mainThread())
                .map(articlesDb -> {
                    articlesList.clear();
                    articlesList.addAll(articlesDb);
                    if (!articlesDb.isEmpty()) {
                        lastID = articlesDb.get(articlesDb.size() - 1).getID();
                        dbOffset = articlesDb.size();
                    }
                    getViewState().notifyDataSetChanged(adapter);
                    return articlesDb;
                })
                .subscribe(articles-> {
                    if (articlesList.isEmpty()) {
                        restApi.getArticles(lastID).subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .map(articlesWeb -> {
                                    articlesList.addAll(articlesWeb);
                                    lastID = articlesList.get(articlesList.size() - 1).getID();
                                    getViewState().notifyDataSetChanged(adapter);
                                    return articlesWeb;
                                })
                                .subscribe(articles1 -> {
                                            insertArticles(articles1)
                                                    .subscribeOn(Schedulers.io())
                                                    .subscribe();
                                        });
                    }
                });

        FirebaseMessaging.getInstance().subscribeToTopic(Constants.TOPIC);

    }

    private Observable insertArticles(List<Article> articles) {
        return new Observable() {
            @Override
            protected void subscribeActual(Observer observer) {
                articlesDao.insertAll(articles);
            }
        };
    }

    @SuppressLint("all")
    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        //TODO This Shit
        int dbLimit = 20;
        articlesDao.getArticlesByDescWithLimitSingle(dbLimit, dbOffset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(articlesDb -> {
                    articlesList.addAll(articlesDb);
                    getViewState().notifyDataSetChanged(adapter);
                    return articlesDb;
                })
                .subscribe(articlesDb -> {
                    if (articlesDb.isEmpty()) {
                        restApi.getArticles(lastID).subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(articles1 ->
                                        insertArticles(articles1)
                                                .subscribeOn(Schedulers.io())
                                                .subscribe()
                                );
                    }

                });
    }
}

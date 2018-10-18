package com.unlogicon.typegram.presenters.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.firebase.messaging.FirebaseMessaging;
import com.unlogicon.typegram.Constants;
import com.unlogicon.typegram.R;
import com.unlogicon.typegram.TgramApplication;
import com.unlogicon.typegram.adapters.MainRecyclerViewAdapter;
import com.unlogicon.typegram.interfaces.activities.MainActivityView;
import com.unlogicon.typegram.interfaces.api.RestApi;
import com.unlogicon.typegram.interfaces.dao.ArticlesDao;
import com.unlogicon.typegram.models.Article;
import com.unlogicon.typegram.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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

    @Inject
    Context context;

    @Inject
    SharedPreferencesUtils preferencesUtils;

    private Menu menu;

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
                .subscribe(articles -> {
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
                                }, this::onError);
                    }
                });

        FirebaseMessaging.getInstance().subscribeToTopic(Constants.TOPIC);
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.TOPIC_DEL);

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
                                .subscribe(articles1 -> {
                                    insertArticles(articles1)
                                            .subscribeOn(Schedulers.io())
                                            .subscribe();
                                }, this::onError);
                    }

                });
    }

    public void onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_in:
                if (!preferencesUtils.isAuth()) {
                    getViewState().startActivityLogin();
                }
                break;
            case R.id.action_log_out:
                preferencesUtils.singOut();
                updateMenuTitles(menu);
                break;
        }
    }

    private void onError(Throwable throwable) {
        Toast.makeText(context, context.getString(R.string.error_network), Toast.LENGTH_LONG).show();
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
        updateMenuTitles(menu);
    }

    private void updateMenuTitles(Menu menu) {
        MenuItem menuSingIn = menu.findItem(R.id.action_sign_in);
        MenuItem menuSingOut = menu.findItem(R.id.action_log_out);
        if (preferencesUtils.isAuth()) {
            menuSingIn.setTitle("@" + preferencesUtils.getUsername());
            menuSingOut.setVisible(true);
        } else {
            menuSingIn.setTitle(context.getString(R.string.sign_in));
            menuSingOut.setVisible(false);
        }
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.create_article:
                if (!preferencesUtils.isAuth()){
                    getViewState().startActivityLogin();
                } else {
                    getViewState().startActivityArticleEditor();
                }
                break;
        }
    }
}

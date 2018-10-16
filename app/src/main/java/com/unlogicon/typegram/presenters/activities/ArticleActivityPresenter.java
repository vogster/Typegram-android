package com.unlogicon.typegram.presenters.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.unlogicon.typegram.R;
import com.unlogicon.typegram.TgramApplication;
import com.unlogicon.typegram.interfaces.activities.ArticleActivityView;
import com.unlogicon.typegram.interfaces.api.RestApi;
import com.unlogicon.typegram.models.Article;
import com.unlogicon.typegram.ui.activities.ArticleActivity;
import com.unlogicon.typegram.utils.DateUtils;
import com.unlogicon.typegram.utils.NetworkUtils;
import com.unlogicon.typegram.utils.StringUtils;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Nikita Korovkin 08.10.2018.
 */
@InjectViewState
public class ArticleActivityPresenter extends MvpPresenter<ArticleActivityView> {

    private Article currentArtcile;

    @Inject
    Context context;

    @Inject
    RestApi restApi;

    private int id;
    private String user;

    public ArticleActivityPresenter() {
        TgramApplication.getInstance().getComponents().getAppComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

    }


    @SuppressLint("all")
    public void onCreate(Intent intent) {
        if (!NetworkUtils.isOnline(context)) {
            getViewState().setErrorLayoutVisibility(View.VISIBLE);
            getViewState().setLoadingLayoutVisibility(View.GONE);
        } else {
            if (intent.hasExtra(ArticleActivity.ID_EXTRA) && intent.hasExtra(ArticleActivity.USER_EXTRA)) {
                id = intent.getIntExtra(ArticleActivity.ID_EXTRA, 0);
                user = intent.getStringExtra(ArticleActivity.USER_EXTRA);
                requestGetArticle();
            }
        }

    }

    private void requestGetArticle(){
        getViewState().setLoadingLayoutVisibility(View.VISIBLE);
        restApi.getArticle(user, id).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError);
    }


    private void onSuccess(Article article) {
        getViewState().setTextArticle(article.getBody());
        getViewState().setTitleText(article.getTitle());
        getViewState().setAuthor("@" + article.getAuthor());
        getViewState().setDate(DateUtils.timeAgo(article.getCreatedAt()));
        getViewState().loadAvatar(article.getAuthor());
        currentArtcile = article;
        getViewState().setLoadingLayoutVisibility(View.GONE);
    }

    private void onError(Throwable throwable) {
        getViewState().setErrorLayoutVisibility(View.VISIBLE);
        getViewState().setLoadingLayoutVisibility(View.GONE);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.retry:
                getViewState().setErrorLayoutVisibility(View.GONE);
                requestGetArticle();
                break;
        }

    }

    public void onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open_in_web:
                if (currentArtcile != null) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(StringUtils.generateUrlFromArticle(currentArtcile)));
                    getViewState().openLinkInBrowser(browserIntent);
                }
                break;
        }
    }
}

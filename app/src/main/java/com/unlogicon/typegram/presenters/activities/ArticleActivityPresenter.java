package com.unlogicon.typegram.presenters.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.gson.Gson;
import com.unlogicon.typegram.R;
import com.unlogicon.typegram.TgramApplication;
import com.unlogicon.typegram.adapters.CommentsAdapter;
import com.unlogicon.typegram.interfaces.activities.ArticleActivityView;
import com.unlogicon.typegram.interfaces.api.RestApi;
import com.unlogicon.typegram.models.Article;
import com.unlogicon.typegram.models.Error;
import com.unlogicon.typegram.models.posts.PostComment;
import com.unlogicon.typegram.watchers.RxTextWatcher;
import com.unlogicon.typegram.ui.activities.ArticleActivity;
import com.unlogicon.typegram.utils.DateUtils;
import com.unlogicon.typegram.utils.NetworkUtils;
import com.unlogicon.typegram.utils.SharedPreferencesUtils;
import com.unlogicon.typegram.utils.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

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

    @Inject
    SharedPreferencesUtils preferencesUtils;

    private int id;
    private String user;

    private CommentsAdapter adapter;
    private List<Article> comments = new ArrayList<>();

    private RxTextWatcher commentTextWatcher;

    public ArticleActivityPresenter() {
        TgramApplication.getInstance().getComponents().getAppComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        adapter = new CommentsAdapter(comments);
        getViewState().setCommentsAdapter(adapter);

        if (preferencesUtils.isAuth()){
            getViewState().setCommentLayoutVisibility(View.VISIBLE);
        } else {
            getViewState().setCommentLayoutVisibility(View.GONE);
        }
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
        if (article.getComments() != null) {
            comments.clear();
            comments.addAll(article.getComments());
            adapter.notifyDataSetChanged();
        }
        getViewState().setTextArticle(article.getBody());
        getViewState().setTitleText(article.getTitle());
        getViewState().setAuthor("@" + article.getAuthor());
        getViewState().setDate(DateUtils.timeAgo(article.getCreatedAt()));
        getViewState().loadAvatar(article.getAuthor());
        currentArtcile = article;
        getViewState().setLoadingLayoutVisibility(View.GONE);
    }

    private void onError(Throwable throwable) {
        getViewState().showSnackbar(throwable.getMessage());
        getViewState().setErrorLayoutVisibility(View.VISIBLE);
        getViewState().setLoadingLayoutVisibility(View.GONE);
    }

    public void setCommentTextWatcher(EditText editText){
        commentTextWatcher = new RxTextWatcher();
        commentTextWatcher.watch(editText);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.retry:
                getViewState().setErrorLayoutVisibility(View.GONE);
                requestGetArticle();
                break;
            case R.id.sendComment:
                restApi.postComment(currentArtcile.getAuthor(), currentArtcile.getID(), new PostComment(commentTextWatcher.getText()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::onSuccessComment, this::onError);
                break;
        }

    }

    private void onSuccessComment(Response<Article> articleResponse) {
        if (articleResponse.code() == 200){
            getViewState().setCommentText("");
            comments.add(articleResponse.body());
            adapter.notifyDataSetChanged();
        } else {
            try {
                getViewState().showSnackbar(new Gson().fromJson(articleResponse.errorBody().string(), Error.class).getError());
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            case R.id.action_share:
                if (currentArtcile != null) {
                    getViewState().share(StringUtils.generateUrlFromArticle(currentArtcile));
                }
                break;
        }
    }
}

package com.unlogicon.typegram.interfaces.activities;

import android.content.Intent;

import com.arellomobile.mvp.MvpView;
import com.unlogicon.typegram.adapters.CommentsAdapter;

/**
 * Nikita Korovkin 08.10.2018.
 */
public interface ArticleActivityView extends MvpView {


    void setErrorLayoutVisibility(int visibility);

    void setLoadingLayoutVisibility(int visibility);

    void setTextArticle(String text);

    void setTitleText(String text);

    void loadAvatar(String author);

    void setAuthor(String author);

    void setDate(String date);

    void openLinkInBrowser(Intent intent);

    void setCommentsAdapter(CommentsAdapter adapter);

    void setCommentLayoutVisibility(int visibility);

    void setCommentText(String text);
}

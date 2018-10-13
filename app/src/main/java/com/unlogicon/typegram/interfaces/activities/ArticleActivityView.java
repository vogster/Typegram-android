package com.unlogicon.typegram.interfaces.activities;

import android.content.Intent;

import com.arellomobile.mvp.MvpView;

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
}

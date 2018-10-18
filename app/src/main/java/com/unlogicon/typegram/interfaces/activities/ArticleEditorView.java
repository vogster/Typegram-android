package com.unlogicon.typegram.interfaces.activities;

import com.arellomobile.mvp.MvpView;

/**
 * Nikita Korovkin 18.10.2018.
 */
public interface ArticleEditorView extends MvpView {

    void setTitleText(String text);

    void setOgimageText(String text);

    void setBodyText(String text);

    void setTagText(String text);

    void showSnackbar(String text);

    void finishActivity();
}

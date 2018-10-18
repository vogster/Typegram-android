package com.unlogicon.typegram.presenters.activities;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.view.MenuItem;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.gson.Gson;
import com.unlogicon.typegram.R;
import com.unlogicon.typegram.TgramApplication;
import com.unlogicon.typegram.enums.EditorEnum;
import com.unlogicon.typegram.interfaces.activities.ArticleEditorView;
import com.unlogicon.typegram.interfaces.api.RestApi;
import com.unlogicon.typegram.models.Error;
import com.unlogicon.typegram.models.posts.PostArticle;
import com.unlogicon.typegram.utils.EditorPreferencesUtils;
import com.unlogicon.typegram.watchers.RxEditorTextWatcher;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Nikita Korovkin 18.10.2018.
 */
@InjectViewState
public class ArticleEditorPresenter extends MvpPresenter<ArticleEditorView> {

    private RxEditorTextWatcher titleTextWatcher;
    private RxEditorTextWatcher ogimageTextWatcher;
    private RxEditorTextWatcher bodyTextWatcher;
    private RxEditorTextWatcher tagTextWatcher;

    @Inject
    EditorPreferencesUtils editorPreferencesUtils;

    @Inject
    RestApi restApi;

    @Inject
    Context context;

    public ArticleEditorPresenter(){
        TgramApplication.getInstance().getComponents().getEditorComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().setTitleText(editorPreferencesUtils.getTitle());
        getViewState().setOgimageText(editorPreferencesUtils.getOgimgae());
        getViewState().setBodyText(editorPreferencesUtils.getBody());
        getViewState().setTagText(editorPreferencesUtils.getTag());
    }

    public void setTitleTextWatcher(AppCompatEditText title) {
        titleTextWatcher = new RxEditorTextWatcher();
        titleTextWatcher.watch(title, EditorEnum.TITLE);
    }

    public void setOgimageTextWatcher(AppCompatEditText ogimage) {
        ogimageTextWatcher = new RxEditorTextWatcher();
        ogimageTextWatcher.watch(ogimage, EditorEnum.OGIMAGE);
    }

    public void setBodyTextWatcher(AppCompatEditText body) {
        bodyTextWatcher = new RxEditorTextWatcher();
        bodyTextWatcher.watch(body, EditorEnum.BODY);
    }

    public void setTagTextWatcher(AppCompatEditText tag) {
        tagTextWatcher = new RxEditorTextWatcher();
        tagTextWatcher.watch(tag, EditorEnum.TAG);
    }

    public void onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_publish:
                restApi.postArticle(new PostArticle(titleTextWatcher.getText()
                        , ogimageTextWatcher.getText(), bodyTextWatcher.getText(), tagTextWatcher.getText()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::onSuccess, this::onError);
                break;
        }
    }

    private void onError(Throwable throwable) {
        getViewState().showSnackbar(context.getString(R.string.error_network));
    }

    private void onSuccess(Response<ResponseBody> responseBodyResponse) {
        if (responseBodyResponse.code() == 200){
            editorPreferencesUtils.clearAllFields();
            getViewState().finishActivity();
        } else {
            try {
                getViewState().showSnackbar(new Gson().fromJson(responseBodyResponse.errorBody().string(), Error.class).getError());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

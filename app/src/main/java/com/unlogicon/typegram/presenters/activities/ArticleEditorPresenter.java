package com.unlogicon.typegram.presenters.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.gson.Gson;
import com.unlogicon.typegram.R;
import com.unlogicon.typegram.TgramApplication;
import com.unlogicon.typegram.adapters.MarkdownToolbarAdapter;
import com.unlogicon.typegram.enums.EditorEnum;
import com.unlogicon.typegram.enums.MarkdownToolBarEnum;
import com.unlogicon.typegram.interfaces.activities.ArticleEditorView;
import com.unlogicon.typegram.interfaces.api.RestApi;
import com.unlogicon.typegram.models.Error;
import com.unlogicon.typegram.models.posts.PostArticle;
import com.unlogicon.typegram.ui.activities.ArticleEditorActivity;
import com.unlogicon.typegram.utils.EditorPreferencesUtils;
import com.unlogicon.typegram.watchers.RxEditorTextWatcher;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    private MarkdownToolbarAdapter adapter;

    public ArticleEditorPresenter(){
        TgramApplication.getInstance().getComponents().getEditorComponent().inject(this);
        adapter = new MarkdownToolbarAdapter();
        adapter.setcLickListeners(this::onClick);
        getViewState().setMarkdownToolbarAdapter(adapter);
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
                case R.id.action_attach_image:
                    getViewState().openImage();
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ArticleEditorActivity.PICK_IMAGE && resultCode == Activity.RESULT_OK) {

            getViewState().showProgressSnackBar();

            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            File file = new File(filePath);

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);

         restApi.uploadImage(body) .observeOn(AndroidSchedulers.mainThread())
                 .subscribeOn(Schedulers.io())
                 .subscribe(this::onSuccessUpload, this::onError);
        }
    }

    private void onSuccessUpload(Response<ResponseBody> responseBodyResponse) {
        if (responseBodyResponse.code() == 200) {
            String s = null;
            try {
                s = responseBodyResponse.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String url = s.substring(s.indexOf("<textarea>") + 10, s.indexOf("</textarea>"));
            getViewState().insertTextTotBody(url);
            getViewState().hideProgressSnackBar();
        }
    }

    public void onClick(MarkdownToolBarEnum toolBarEnum) {
        switch (toolBarEnum){
            case ATTACH:
                getViewState().openImage();
                break;
        }
    }
}

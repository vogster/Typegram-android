package com.unlogicon.typegram.markdown;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.unlogicon.typegram.R;
import com.unlogicon.typegram.TgramApplication;
import com.unlogicon.typegram.enums.MarkdownToolBarEnum;
import com.unlogicon.typegram.interfaces.api.RestApi;
import com.unlogicon.typegram.ui.activities.ArticleEditorActivity;

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
 * Nikita Korovkin 31.10.2018.
 */
public class MarkdownTextActions {
    
    private Activity activity;
    
    private EditText editText;

    private Snackbar preogressSnackBar;

    public static final int PICK_IMAGE = 100;

    @Inject
    RestApi restApi;
    
    public MarkdownTextActions(Activity activity, EditText editText){
        TgramApplication.getInstance().getComponents().getAppComponent().inject(this);
        this.activity = activity;
        this.editText = editText;

        preogressSnackBar = Snackbar.make(editText, "Uploading...", Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout snack_view = (Snackbar.SnackbarLayout) preogressSnackBar.getView();
        snack_view.addView(new ProgressBar(activity));
    }
    
    public void onClick(MarkdownToolBarEnum toolBarEnum){
        switch (toolBarEnum){
            case ATTACH:
                openImage();
                break;
            case H1:
                runMarkdownRegularPrefixAction("# ");
                break;
            case H2:
                runMarkdownRegularPrefixAction("## ");
                break;
            case H3:
                runMarkdownRegularPrefixAction("### ");
                break;
            case BOLD:
                runMarkdownInlineAction("**");
                break;
            case CODE:
                runMarkdownInlineAction("`");
                break;
            case LINK:
                break;
            case LIST:
                runMarkdownRegularPrefixAction("- ");
                break;
            case MORE:
                runMarkdownInlineAction("----\n");
                break;
            case ITALIC:
                runMarkdownInlineAction("_");
                break;
            case STRIKETHROUGHT:
                runMarkdownInlineAction("~~");
                break;
            case QUOTE:
                runMarkdownRegularPrefixAction("> ");
                break;
            case NUMBER_LIST:
                runMarkdownRegularPrefixAction("1. ");
                break;
        }
    }

    private void openImage() {
        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        activity.startActivityForResult(Intent.createChooser(i, "Select Image"), PICK_IMAGE);
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {showSnackbar(activity.getString(R.string.need_permissions));}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    public void showSnackbar(String text) {
        Snackbar.make(editText, text, Snackbar.LENGTH_LONG)
                .show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ArticleEditorActivity.PICK_IMAGE && resultCode == Activity.RESULT_OK) {

            showProgressSnackBar(true);

            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = activity.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            File file = new File(filePath);

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);

            restApi.uploadImage(body).observeOn(AndroidSchedulers.mainThread())
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
            insertTextToBody(url);
            showProgressSnackBar(false);
        }
    }

    private void insertTextToBody(String url) {
        int start = Math.max(editText.getSelectionStart(), 0);
        int end = Math.max(editText.getSelectionEnd(), 0);
        editText.getText().replace(Math.min(start, end), Math.max(start, end),
                url, 0, url.length());
    }

    private void onError(Throwable throwable) {
    }


    private void showProgressSnackBar(boolean b) {
        if (b){
            preogressSnackBar.show();
        } else {
            preogressSnackBar.dismiss();
        }
    }

    private void runMarkdownRegularPrefixAction(String action) {
        if (editText.hasSelection()) {
            String text = editText.getText().toString();
            int selectionStart = editText.getSelectionStart();
            int selectionEnd = editText.getSelectionEnd();

            //Check if Selection includes the shortcut characters
            if (text.substring(selectionStart, selectionEnd)
                    .matches("(>|#{1,3}|-|[1-9]\\.)(\\s)?[a-zA-Z0-9\\s]*")) {

                text = text.substring(selectionStart + action.length(), selectionEnd);
                editText.getText()
                        .replace(selectionStart, selectionEnd, text);

            }
            //Check if Selection is Preceded by shortcut characters
            else if ((selectionStart >= action.length()) && (text.substring(selectionStart - action.length(), selectionEnd)
                    .matches("(>|#{1,3}|-|[1-9]\\.)(\\s)?[a-zA-Z0-9\\s]*"))) {

                text = text.substring(selectionStart, selectionEnd);
                editText.getText()
                        .replace(selectionStart - action.length(), selectionEnd, text);

            }
            //Condition to insert shortcut preceding the selection
            else {
                editText.getText().insert(selectionStart, action);
            }
        } else {
            //Condition for Empty Selection. Should insert the action at the start of the line
            int cursor = editText.getSelectionStart();
            int i = cursor - 1;
            Editable s = editText.getText();
            for (; i >= 0; i--) {
                if (s.charAt(i) == '\n') {
                    break;
                }
            }

            s.insert(i + 1, action);
        }
    }


    private void runMarkdownInlineAction(String _action) {
        if (editText.hasSelection()) {
            String text = editText.getText().toString();
            int selectionStart = editText.getSelectionStart();
            int selectionEnd = editText.getSelectionEnd();

            //Check if Selection includes the shortcut characters
            if (selectionEnd < text.length() && selectionStart >= 0 && (text.substring(selectionStart, selectionEnd)
                    .matches("(\\*\\*|~~|_|`)[a-zA-Z0-9\\s]*(\\*\\*|~~|_|`)"))) {

                text = text.substring(selectionStart + _action.length(),
                        selectionEnd - _action.length());
                editText.getText()
                        .replace(selectionStart, selectionEnd, text);

            }
            //Check if Selection is Preceded and succeeded by shortcut characters
            else if (((selectionEnd <= (editText.length() - _action.length())) &&
                    (selectionStart >= _action.length())) &&
                    (text.substring(selectionStart - _action.length(),
                            selectionEnd + _action.length())
                            .matches("(\\*\\*|~~|_|`)[a-zA-Z0-9\\s]*(\\*\\*|~~|_|`)"))) {

                text = text.substring(selectionStart, selectionEnd);
                editText.getText()
                        .replace(selectionStart - _action.length(),
                                selectionEnd + _action.length(), text);

            }
            //Condition to insert shortcut preceding and succeeding the selection
            else {
                editText.getText().insert(selectionStart, _action);
                editText.getText().insert(editText.getSelectionEnd(), _action);
            }
        } else {
            //Condition for Empty Selection
            if (false) {
                // Condition for things that should only be placed at the start of the line even if no text is selected
            } else if (_action.equals("----\n")) {
                editText.getText().insert(editText.getSelectionStart(), _action);
            } else {
                // Condition for formatting which is inserted on either side of the cursor
                editText.getText().insert(editText.getSelectionStart(), _action)
                        .insert(editText.getSelectionEnd(), _action);
                editText.setSelection(editText.getSelectionStart() - _action.length());
            }
        }
    }

}

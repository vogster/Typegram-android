package com.unlogicon.typegram.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ProgressBar;

import com.arellomobile.mvp.MvpActivity;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.unlogicon.typegram.R;
import com.unlogicon.typegram.TgramApplication;
import com.unlogicon.typegram.adapters.MainRecyclerViewAdapter;
import com.unlogicon.typegram.adapters.MarkdownToolbarAdapter;
import com.unlogicon.typegram.interfaces.activities.ArticleEditorView;
import com.unlogicon.typegram.presenters.activities.ArticleEditorPresenter;

import java.io.File;

/**
 * Nikita Korovkin 18.10.2018.
 */
public class ArticleEditorActivity extends MvpAppCompatActivity implements ArticleEditorView {
    @InjectPresenter
    ArticleEditorPresenter presenter;

    private AppCompatEditText title, ogimage, body, tag;

    public static final int PICK_IMAGE = 100;

    private Snackbar preogressSnackBar;

    private RecyclerView markdownToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_article_editor);


        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setProgressBarIndeterminateVisibility(true);

        title = findViewById(R.id.title);
        ogimage = findViewById(R.id.ogimage);
        body = findViewById(R.id.body);
        tag = findViewById(R.id.tag);

        presenter.setTitleTextWatcher(title);
        presenter.setOgimageTextWatcher(ogimage);
        presenter.setBodyTextWatcher(body);
        presenter.setTagTextWatcher(tag);

        markdownToolbar = findViewById(R.id.markdown_toolbar);
        markdownToolbar.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));



        preogressSnackBar = Snackbar.make(body, "Uploading...", Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout snack_view = (Snackbar.SnackbarLayout) preogressSnackBar.getView();
        snack_view.addView(new ProgressBar(this));
    }

    @Override
    public void setTitleText(String text) {
        title.setText(text);
    }

    @Override
    public void setOgimageText(String text) {
        ogimage.setText(text);
    }

    @Override
    public void setBodyText(String text) {
        body.setText(text);
    }

    @Override
    public void setTagText(String text) {
        tag.setText(text);
    }

    @Override
    public void showSnackbar(String text) {
        Snackbar.make(tag, text, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void finishActivity() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void openImage() {

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(Intent.createChooser(i, "Select Image"), PICK_IMAGE);
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {showSnackbar(getString(R.string.need_permissions));}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }

    @Override
    public void showProgressSnackBar() {
        preogressSnackBar.show();
    }

    @Override
    public void hideProgressSnackBar() {
        preogressSnackBar.dismiss();
    }

    @Override
    public void setMarkdownToolbarAdapter(MarkdownToolbarAdapter markdownToolbarAdapter) {
        markdownToolbar.setAdapter(markdownToolbarAdapter);
    }

    @Override
    public void insertTextTotBody(String text) {
        int start = Math.max(body.getSelectionStart(), 0);
        int end = Math.max(body.getSelectionEnd(), 0);
        body.getText().replace(Math.min(start, end), Math.max(start, end),
                text, 0, text.length());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        presenter.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        TgramApplication.getInstance().getComponents().removeEditorComponent();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }
}

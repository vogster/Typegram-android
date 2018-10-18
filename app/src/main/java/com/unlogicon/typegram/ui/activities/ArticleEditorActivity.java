package com.unlogicon.typegram.ui.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatEditText;
import android.view.Menu;
import android.view.MenuItem;

import com.arellomobile.mvp.MvpActivity;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.unlogicon.typegram.R;
import com.unlogicon.typegram.TgramApplication;
import com.unlogicon.typegram.interfaces.activities.ArticleEditorView;
import com.unlogicon.typegram.presenters.activities.ArticleEditorPresenter;

/**
 * Nikita Korovkin 18.10.2018.
 */
public class ArticleEditorActivity extends MvpAppCompatActivity implements ArticleEditorView {
    @InjectPresenter
    ArticleEditorPresenter presenter;

    private AppCompatEditText title, ogimage, body, tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_editor);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        title = findViewById(R.id.title);
        ogimage = findViewById(R.id.ogimage);
        body = findViewById(R.id.body);
        tag = findViewById(R.id.tag);

        presenter.setTitleTextWatcher(title);
        presenter.setOgimageTextWatcher(ogimage);
        presenter.setBodyTextWatcher(body);
        presenter.setTagTextWatcher(tag);
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
        finish();
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
}

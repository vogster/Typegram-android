package com.unlogicon.typegram.ui.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.unlogicon.typegram.R;
import com.unlogicon.typegram.interfaces.activities.RegisterView;
import com.unlogicon.typegram.presenters.activities.RegisterPresenter;

public class RegisterActivity extends MvpAppCompatActivity implements RegisterView {

    @InjectPresenter
    RegisterPresenter presenter;

    private AppCompatEditText username, password;

    private AppCompatCheckBox chackbox_1, checkbox_2, checkbox_3;

    private AppCompatButton sing_up;

    private AppCompatTextView allready_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        username = findViewById(R.id.username);
        username.setFilters(new InputFilter[] {
                new InputFilter.AllCaps() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        return String.valueOf(source).toLowerCase();
                    }
                }
        });
        password = findViewById(R.id.password);

        presenter.setUsernameTextWatcher(username);
        presenter.setPasswordTextWatcher(password);

        chackbox_1 = findViewById(R.id.checkbox_1);

        checkbox_2 = findViewById(R.id.checkbox_2);
        checkbox_2.setText(Html.fromHtml(getString(R.string.sign_up_checkbox_2)
                 + " "+ "<a href='https://ru.tgr.am/policy'>Typegram Privacy Statement</a>"));
        checkbox_2.setMovementMethod(LinkMovementMethod.getInstance());

        checkbox_3 = findViewById(R.id.checkbox_3);
        checkbox_3.setText(Html.fromHtml(getString(R.string.sign_up_checkbox_3)
                + " "+ "<a href='https://ru.tgr.am/terms'>Typegram Terms of Service</a>"));
        checkbox_3.setMovementMethod(LinkMovementMethod.getInstance());

        chackbox_1.setOnCheckedChangeListener(presenter::checkBoxChangeListener);
        checkbox_2.setOnCheckedChangeListener(presenter::checkBoxChangeListener);
        checkbox_3.setOnCheckedChangeListener(presenter::checkBoxChangeListener);

        sing_up = findViewById(R.id.sign_up);
        sing_up.setOnClickListener(presenter::onClick);

        allready_account = findViewById(R.id.already_account);
        allready_account.setPaintFlags(allready_account.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        allready_account.setOnClickListener(presenter::onClick);

    }

    @Override
    public void setSingUpEnabled(boolean enabled) {
        sing_up.setEnabled(enabled);
    }

    @Override
    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void showSnackbar(String text) {
        Snackbar.make(username, text, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

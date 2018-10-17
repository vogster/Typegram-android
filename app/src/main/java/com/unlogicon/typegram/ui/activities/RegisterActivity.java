package com.unlogicon.typegram.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
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
}

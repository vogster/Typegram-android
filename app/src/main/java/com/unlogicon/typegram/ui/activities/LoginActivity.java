package com.unlogicon.typegram.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.Menu;
import android.view.MenuItem;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.unlogicon.typegram.R;
import com.unlogicon.typegram.interfaces.activities.LoginView;
import com.unlogicon.typegram.presenters.activities.LoginPresenter;

/**
 * Nikita Korovkin 16.10.2018.
 */
public class LoginActivity extends MvpAppCompatActivity implements LoginView {

    @InjectPresenter
    LoginPresenter presenter;

    private AppCompatEditText username, password;
    private AppCompatButton login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        presenter.setUsernameTextWatcher(username);

        password = findViewById(R.id.password);
        presenter.setPasswordTextWatcher(password);

        login = findViewById(R.id.login);
        login.setOnClickListener(presenter::onClick);
    }

    @Override
    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

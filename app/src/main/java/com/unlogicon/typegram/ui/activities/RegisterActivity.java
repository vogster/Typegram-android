package com.unlogicon.typegram.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;

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

        chackbox_1 = findViewById(R.id.checkbox_1);
        checkbox_2 = findViewById(R.id.checkbox_2);
        checkbox_3 = findViewById(R.id.checkbox_3);
        chackbox_1.setOnCheckedChangeListener(presenter::checkBoxChangeListener);
        checkbox_2.setOnCheckedChangeListener(presenter::checkBoxChangeListener);
        checkbox_3.setOnCheckedChangeListener(presenter::checkBoxChangeListener);

        sing_up = findViewById(R.id.sign_up);
        sing_up.setOnClickListener(presenter::onClick);
    }
}

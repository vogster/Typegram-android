package com.unlogicon.typegram.presenters.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.unlogicon.typegram.R;
import com.unlogicon.typegram.TgramApplication;
import com.unlogicon.typegram.interfaces.activities.LoginView;
import com.unlogicon.typegram.interfaces.api.RestApi;
import com.unlogicon.typegram.models.Login;
import com.unlogicon.typegram.tools.RxTextWatcher;
import com.unlogicon.typegram.utils.SharedPreferencesUtils;
import com.unlogicon.typegram.utils.StringUtils;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Nikita Korovkin 16.10.2018.
 */
@InjectViewState
public class LoginPresenter extends MvpPresenter<LoginView> {
    
    @Inject
    RestApi restApi;
    
    @Inject
    SharedPreferencesUtils preferencesUtils;

    @Inject
    Context context;

    private RxTextWatcher usernameTextWatcher;
    private RxTextWatcher passwordTextWatcher;
    
    public LoginPresenter(){
        TgramApplication.getInstance().getComponents().getAppComponent().inject(this);
    }
    
    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    public void onClick(View view) {
        restApi.login(new Login(usernameTextWatcher.getText(), passwordTextWatcher.getText()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::onSuccess, this::onError);
    }

    private void onSuccess(ResponseBody responseBody) {
        try {
            Toast.makeText(context, responseBody.string(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onError(Throwable throwable) {
        Log.d("Error", throwable.getLocalizedMessage());
    }

    public void setUsernameTextWatcher(EditText editText){
        usernameTextWatcher = new RxTextWatcher();
        usernameTextWatcher.watch(editText);
    }

    public void setPasswordTextWatcher(EditText editText){
        passwordTextWatcher = new RxTextWatcher();
        passwordTextWatcher.watch(editText);
    }


}

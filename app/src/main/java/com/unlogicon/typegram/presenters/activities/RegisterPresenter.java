package com.unlogicon.typegram.presenters.activities;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.unlogicon.typegram.R;
import com.unlogicon.typegram.TgramApplication;
import com.unlogicon.typegram.interfaces.activities.RegisterView;
import com.unlogicon.typegram.interfaces.api.RestApi;
import com.unlogicon.typegram.models.Register;
import com.unlogicon.typegram.tools.RxTextWatcher;
import com.unlogicon.typegram.utils.SharedPreferencesUtils;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

@InjectViewState
public class RegisterPresenter extends MvpPresenter<RegisterView> {

    @Inject
    RestApi restApi;

    @Inject
    Context context;

    @Inject
    SharedPreferencesUtils preferencesUtils;

    private boolean check1;
    private boolean check2;
    private boolean check3;

    private RxTextWatcher usernameTextWatcher;
    private RxTextWatcher passwordTextWatcher;

    public RegisterPresenter(){
        TgramApplication.getInstance().getComponents().getAppComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }


    public void onClick(View view) {
        restApi.register(new Register(usernameTextWatcher.getText(), passwordTextWatcher.getText(), "privacy", "terms"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::onSuccess, this::onError);

    }

    private void onSuccess(ResponseBody responseBody) {
        if (responseBody != null){
            try {
                preferencesUtils.setToken(responseBody.string().replace("\"",""));
                preferencesUtils.setUsername(usernameTextWatcher.getText());
                getViewState().startMainActivity();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onError(Throwable throwable) {
        Log.d("","");
    }


    public void setUsernameTextWatcher(EditText editText){
        usernameTextWatcher = new RxTextWatcher();
        usernameTextWatcher.watch(editText);
    }

    public void setPasswordTextWatcher(EditText editText){
        passwordTextWatcher = new RxTextWatcher();
        passwordTextWatcher.watch(editText);
    }


    public void checkBoxChangeListener(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.checkbox_1:
                check1 = b;
                break;
            case R.id.checkbox_2:
                check2 = b;
                break;
            case R.id.checkbox_3:
                check3 = b;
                break;
        }

        updateStateSingUpButton();
    }

    private void updateStateSingUpButton() {
        if (!check1 && check2 && check3){
            getViewState().setSingUpEnabled(true);
        } else {
            getViewState().setSingUpEnabled(false);
        }
    }
}

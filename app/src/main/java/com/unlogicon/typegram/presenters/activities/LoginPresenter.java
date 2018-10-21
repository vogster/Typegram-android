package com.unlogicon.typegram.presenters.activities;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.gson.Gson;
import com.unlogicon.typegram.R;
import com.unlogicon.typegram.TgramApplication;
import com.unlogicon.typegram.interfaces.activities.LoginView;
import com.unlogicon.typegram.interfaces.api.RestApi;
import com.unlogicon.typegram.models.Error;
import com.unlogicon.typegram.models.posts.PostLogin;
import com.unlogicon.typegram.watchers.RxTextWatcher;
import com.unlogicon.typegram.utils.SharedPreferencesUtils;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

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

        switch (view.getId()){
            case R.id.login:
                restApi.login(new PostLogin(usernameTextWatcher.getText(), passwordTextWatcher.getText()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::onSuccess, this::onError);
                break;
            case R.id.needAnAcc:
                getViewState().startRegisterActivity();
                break;
        }

    }

    private void onSuccess(Response<ResponseBody> responseBodyResponse) {
        if (responseBodyResponse.code() == 200){
            try {
                preferencesUtils.setToken(responseBodyResponse.body().string().replace("\"",""));
                preferencesUtils.setUsername(usernameTextWatcher.getText());
                getViewState().startMainActivity();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                getViewState().showSnackbar(new Gson().fromJson(responseBodyResponse.errorBody().string(), Error.class).getError());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onError(Throwable throwable) {

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

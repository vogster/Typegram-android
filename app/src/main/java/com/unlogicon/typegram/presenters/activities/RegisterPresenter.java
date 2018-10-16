package com.unlogicon.typegram.presenters.activities;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.unlogicon.typegram.TgramApplication;
import com.unlogicon.typegram.interfaces.activities.RegisterView;
import com.unlogicon.typegram.interfaces.api.RestApi;
import com.unlogicon.typegram.models.Register;
import com.unlogicon.typegram.utils.SharedPreferencesUtils;

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

    public RegisterPresenter(){
        TgramApplication.getInstance().getComponents().getAppComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }


    public void onClick(View view) {
        restApi.register(new Register("hahaha", "1234567890", true, true))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::onSuccess, this::onError);

    }

    private void onSuccess(ResponseBody responseBody) {
        Log.d("","");
    }

    private void onError(Throwable throwable) {
        Log.d("","");
    }



    public void checkBoxChangeListener(CompoundButton compoundButton, boolean b) {

    }
}

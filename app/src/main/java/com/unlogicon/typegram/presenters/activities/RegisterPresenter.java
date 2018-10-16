package com.unlogicon.typegram.presenters.activities;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.unlogicon.typegram.TgramApplication;
import com.unlogicon.typegram.interfaces.activities.RegisterView;
import com.unlogicon.typegram.interfaces.api.RestApi;
import com.unlogicon.typegram.utils.SharedPreferencesUtils;

import javax.inject.Inject;

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


}

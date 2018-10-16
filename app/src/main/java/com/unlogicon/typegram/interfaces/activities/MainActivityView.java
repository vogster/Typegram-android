package com.unlogicon.typegram.interfaces.activities;

import com.arellomobile.mvp.MvpView;
import com.unlogicon.typegram.adapters.MainRecyclerViewAdapter;
import com.unlogicon.typegram.views.EndlessRecyclerViewScrollListener;

public interface MainActivityView extends MvpView {

    void setAdapter(MainRecyclerViewAdapter adapter);

    void notifyDataSetChanged(MainRecyclerViewAdapter adapter);

    void addOnScrollListener(EndlessRecyclerViewScrollListener listener);

    void startActivityLogin();
}

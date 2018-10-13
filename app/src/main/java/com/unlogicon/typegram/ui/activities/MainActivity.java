package com.unlogicon.typegram.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.unlogicon.typegram.R;
import com.unlogicon.typegram.adapters.MainRecyclerViewAdapter;
import com.unlogicon.typegram.interfaces.activities.MainActivityView;
import com.unlogicon.typegram.presenters.activities.MainActivityPresenter;
import com.unlogicon.typegram.views.EndlessRecyclerViewScrollListener;

public class MainActivity extends MvpAppCompatActivity implements MainActivityView {

    @InjectPresenter
    MainActivityPresenter presenter;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.onLoadMore(page, totalItemsCount, view);
            }
        });

    }

    @Override
    public void setAdapter(MainRecyclerViewAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void notifyDataSetChanged(MainRecyclerViewAdapter adapter) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addOnScrollListener(EndlessRecyclerViewScrollListener listener) {
        recyclerView.addOnScrollListener(listener);
    }
}

package com.korzh.hometask.backgroundjob;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.korzh.hometask.backgroundjob.adapter.Adapter;
import com.korzh.hometask.backgroundjob.api.retrofit.Api;
import com.korzh.hometask.backgroundjob.api.acynctask.ApiAsync;
import com.korzh.hometask.backgroundjob.api.retrofit.ApiCreator;
import com.korzh.hometask.backgroundjob.callback.GetItemsCallBack;
import com.korzh.hometask.backgroundjob.model.Model;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final boolean asyncTask = true; //change this for work with retrofit
    private static final int LIMIT = 20;
    private int page = 1;
    private boolean lastPage;
    private Api api;
    private Adapter mAdapter;
    private ProgressDialog mDialog;
    @BindView(R.id.my_recycler_view) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initRecyclerView();
        createApi();
        getPageRequest(page);
    }

    GetItemsCallBack mGetItemsCallBack = new GetItemsCallBack() {
        @Override
        public void onGetItems(List<Model> models) {
            addItems(models);
        }
        @Override
        public void onGetError(String error) {
            Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
        }
    };

    private void getPageRequest(int page) {
        if (!lastPage) {
            showProgress();
            if (asyncTask) {
                new ApiAsync(page, LIMIT, mGetItemsCallBack).execute();
            } else {
                api.getPage(page, LIMIT)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::addItems);
            }
        }
    }

    private void addItems(List<Model> models) {
        hideProgress();
        if (models.size() != 0) {
            mAdapter.addItems(models);
        } else {
            lastPage = true;
        }
    }

    private void initViews() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    private void createApi() {
        if (api == null) {
            api = ApiCreator.createRetrofit();
        }
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new Adapter(() -> getPageRequest(++page));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void showProgress() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage(getString(R.string.please_wait));
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setIndeterminate(true);
        mDialog.show();
    }

    public void hideProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
}

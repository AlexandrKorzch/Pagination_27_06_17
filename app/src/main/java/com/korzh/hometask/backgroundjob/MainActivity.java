package com.korzh.hometask.backgroundjob;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.korzh.hometask.backgroundjob.adapter.Adapter;
import com.korzh.hometask.backgroundjob.api.Api;
import com.korzh.hometask.backgroundjob.api.ApiCreator;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final int LIMIT = 20;

    private int page = 1;

    private boolean lastPage;

    private Api api;
    private Adapter mAdapter;

    @BindView(R.id.my_recycler_view)
    RecyclerView mRecyclerView;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initRecyclerView();
        createApi();
        getPageRequest(page);
    }

    private void getPageRequest(int page) {
        if (!lastPage) {
            showProgress();
            api.getPage(page, LIMIT)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(models -> {
                        if (models.size() != 0) {
                            mAdapter.addItems(models);
                        } else {
                            lastPage = true;
                        }
                        hideProgress();
                    });
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

package com.korzh.hometask.backgroundjob.callback;

import com.korzh.hometask.backgroundjob.model.Model;

import java.util.List;

/**
 * Created by akorzh on 29.06.2017.
 */

public interface GetItemsCallBack {
    void onGetItems(List<Model> models);
    void onGetError(String error);
}

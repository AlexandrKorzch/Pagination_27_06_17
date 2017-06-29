package com.korzh.hometask.backgroundjob.api.retrofit;

import com.korzh.hometask.backgroundjob.model.Model;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

import static com.korzh.hometask.backgroundjob.api.ApiSettings.LIMIT;
import static com.korzh.hometask.backgroundjob.api.ApiSettings.PAGE;
import static com.korzh.hometask.backgroundjob.api.ApiSettings.POSTS;


/**
 * Created by alex on 6/28/17.
 */

public interface Api {

    @GET(POSTS)
    Observable<List<Model>> getPage(
            @Query(PAGE) int  page,
            @Query(LIMIT) int limit);

}

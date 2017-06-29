package com.korzh.hometask.backgroundjob.api.acynctask;

import android.net.Uri;

import com.korzh.hometask.backgroundjob.model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.korzh.hometask.backgroundjob.api.ApiSettings.BASE_URL;
import static com.korzh.hometask.backgroundjob.api.ApiSettings.LIMIT;
import static com.korzh.hometask.backgroundjob.api.ApiSettings.PAGE;
import static com.korzh.hometask.backgroundjob.api.ApiSettings.POSTS;

/**
 * Created by akorzh on 29.06.2017.
 */

class AsyncUtil {

    static String getUrl(int page, int limit) {
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter(PAGE, String.valueOf(page))
                .appendQueryParameter(LIMIT, String.valueOf(limit));
        return BASE_URL + POSTS +"?"+builder.build().getEncodedQuery();
    }

    static  void parseJsonToCollection(String json, List<Model> models) {
        try {
            JSONArray array = new JSONArray(json);
            for(int i = 0; i<array.length(); i++){
                JSONObject obj = array.getJSONObject(i);
                Model model = new Model();
                model.setUserId(obj.getString("userId"));
                model.setId(obj.getString("id"));
                model.setTitle(obj.getString("title"));
                model.setBody(obj.getString("body"));
                models.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

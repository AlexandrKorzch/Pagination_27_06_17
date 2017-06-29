package com.korzh.hometask.backgroundjob.api.acynctask;

import android.os.AsyncTask;

import com.korzh.hometask.backgroundjob.callback.GetItemsCallBack;
import com.korzh.hometask.backgroundjob.model.Model;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static com.korzh.hometask.backgroundjob.api.acynctask.AsyncUtil.getUrl;
import static com.korzh.hometask.backgroundjob.api.acynctask.AsyncUtil.parseJsonToCollection;


/**
 * Created by akorzh on 29.06.2017.
 */

public class ApiAsync extends AsyncTask<Void, List<Model>, List<Model>> {

    private int page;
    private int limit;
    private GetItemsCallBack getItemsCallBack;

    public ApiAsync(int page, int limit, GetItemsCallBack getItemsCallBack) {
        this.page = page;
        this.limit = limit;
        this.getItemsCallBack = getItemsCallBack;
    }

    @Override
    protected List<Model> doInBackground(Void... voids) {
        List<Model> models = new ArrayList<>();
        HttpsURLConnection urlConnection = null;

        try {
            URL url = new URL(getUrl(page, limit));
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200 && urlConnection.getResponseMessage().equalsIgnoreCase("OK")) {
                InputStream it = new BufferedInputStream(urlConnection.getInputStream());
                InputStreamReader read = new InputStreamReader(it);
                BufferedReader buff = new BufferedReader(read);
                StringBuilder dta = new StringBuilder();
                String chunks;
                while ((chunks = buff.readLine()) != null) {
                    dta.append(chunks);
                }
                parseJsonToCollection(dta.toString(), models);
            } else {
                getItemsCallBack.onGetError("Error");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return models;
    }

    @Override
    protected void onPostExecute(List<Model> models) {
        super.onPostExecute(models);
        getItemsCallBack.onGetItems(models);
    }
}

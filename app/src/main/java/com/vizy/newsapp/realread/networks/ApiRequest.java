package com.vizy.newsapp.realread.networks;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by dell on 7/25/2016.
 */
public class ApiRequest {
    String url;
    String json = "";
    String news = "";
    public static final String TAG = "ApiRequest";

    public ApiRequest(String url) {
        this.url = url;
    }

    public String getJSON() {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Response responses) throws IOException {

                    try {
                        json = responses.body().string();
                        JSONObject obj = new JSONObject(json);
                        JSONArray arr = obj.getJSONArray("articles");
                        news = arr.toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            // Log.e(TAG,e.getMessage().toString());
            e.printStackTrace();
        }
    return news;
}
}

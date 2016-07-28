package com.vizy.newsapp.realread.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.vizy.newsapp.realread.About_Us;
import com.vizy.newsapp.realread.R;
import com.vizy.newsapp.realread.RealReadAPI;
import com.vizy.newsapp.realread.adapter.ArticleAdapter;
import com.vizy.newsapp.realread.model.Article;
import com.vizy.newsapp.realread.ui.customview.CarouselLayoutManager;
import com.vizy.newsapp.realread.ui.customview.CarouselZoomPostLayoutListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private String json = "";
    private CarouselLayoutManager carouselLayoutManager;
    private RecyclerView newsCardList;
    private List<Article> newsList;
    private ArticleAdapter articleAdapter;
    private String news;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        newsList = new ArrayList<Article>();
        newsCardList = (RecyclerView) findViewById(R.id.news_list);
        carouselLayoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        carouselLayoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                //  articleAdapter.notifyDataSetChanged();


                articleAdapter = new ArticleAdapter(newsList, MainActivity.this);

                newsCardList.setLayoutManager(carouselLayoutManager);
                newsCardList.setHasFixedSize(true);
                newsCardList.setAdapter(articleAdapter);
                return false;
            }
        });


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(RealReadAPI.NEWS_RESULT)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response responses) {

                if (responses.isSuccessful()) {
                    try {
                        json = responses.body().string();
                        JSONObject obj = new JSONObject(json);
                        JSONArray arr = obj.getJSONArray("articles");
                        news = arr.toString();


                        JSONArray newsArray = new JSONArray(news);

                        for (int i = 0; i < newsArray.length(); i++) {
                            JSONObject jsonObject = newsArray.getJSONObject(i);

                            Article article = new Article();
                            article.setAuthor(jsonObject.getString("author"));
                            article.setDescription(jsonObject.getString("description"));
                            article.setPublishedAt(jsonObject.getString("publishedAt"));
                            article.setTitle(jsonObject.getString("title"));
                            article.setUrl(jsonObject.getString("url"));
                            article.setUrlToImage(jsonObject.getString("urlToImage"));
                            newsList.add(article);
                            Log.e("newsList", newsList.size() + "");
                        }


                    } catch (Exception e) {
                        //    e.printStackTrace();
                    }


                    Message msg = handler.obtainMessage();
                    msg.sendToTarget();

                }

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_us:
                Intent i = new Intent(this, About_Us.class);
                startActivity(i);
                return true;
            case R.id.feedback:
                Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

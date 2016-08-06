package com.vizy.newsapp.realread.activities;

import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
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
import android.view.View;
import android.widget.ProgressBar;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.vizy.newsapp.realread.About_Us;
import com.vizy.newsapp.realread.R;
import com.vizy.newsapp.realread.RealReadAPI;
import com.vizy.newsapp.realread.adapter.ArticleAdapter;
import com.vizy.newsapp.realread.database.QuoteProvider;
import com.vizy.newsapp.realread.model.Article;
import com.vizy.newsapp.realread.ui.customview.CarouselLayoutManager;
import com.vizy.newsapp.realread.ui.customview.CarouselZoomPostLayoutListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String TAG = MainActivity.this.getClass().getSimpleName();
    private String json = "";
    private CarouselLayoutManager carouselLayoutManager;
    private RecyclerView newsCardList;
    private List<Article> newsList;
    private ArticleAdapter articleAdapter;
    private String news;
    private ProgressBar progressBar;
    private Context context;
    private Handler handler;
    private Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        newsList = new ArrayList<Article>();
        newsCardList = (RecyclerView) findViewById(R.id.news_list);
        carouselLayoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        carouselLayoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());


        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                //  progressBar.setVisibility(View.VISIBLE);
                getLoaderManager().initLoader(0, null, MainActivity.this);

                return false;
            }
        });


        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(5, TimeUnit.SECONDS);
        Request request = new Request.Builder()
                .url(RealReadAPI.NEWS_RESULT)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                Message msg = handler.obtainMessage();
                msg.sendToTarget();
            }

            @Override
            public void onResponse(Response responses) {

                if (responses.isSuccessful()) {
                    progressBar.setVisibility(View.VISIBLE);
                    try {
                        json = responses.body().string();
                        JSONObject obj = new JSONObject(json);
                        JSONArray arr = obj.getJSONArray("articles");
                        news = arr.toString();


                        JSONArray newsArray = new JSONArray(news);

                        for (int i = 0; i < newsArray.length(); i++) {

                            int presence = 1;
                            JSONObject jsonObject = newsArray.getJSONObject(i);

                            String newsTitle = jsonObject.getString("title");
                            String newsDescription = jsonObject.getString("description");
                            String newsAuthor = jsonObject.getString("author");
                            String newsPublishedAt = jsonObject.getString("publishedAt");
                            String newsUrl = jsonObject.getString("url");
                            String newsUrlToImage = jsonObject.getString("urlToImage");

                            c = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI, null, null, null, null);
                            c.moveToFirst();

                            if (c.getCount() > 0) {
                                do {

                                    if (newsTitle.equalsIgnoreCase(c.getString(c.getColumnIndex("newsTitle")))) {
                                        presence = 0;
                                        break;
                                    }
                                } while (c.moveToNext());
                            }


                            if (presence == 1) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("newsTitle", newsTitle.toString());
                                contentValues.put("newsDescription", newsDescription.toString());
                                contentValues.put("newsImageUrl", newsUrlToImage.toString());

                                getContentResolver().insert(QuoteProvider.Quotes.CONTENT_URI, contentValues);
                                Log.e(TAG + "Database Error", newsTitle + newsDescription);
                            }
                        }
                        c.close();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage().toString());
                    }

                }

                Message msg = handler.obtainMessage();
                msg.sendToTarget();
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
                Intent i = new Intent(context, About_Us.class);
                startActivity(i);
                return true;
            case R.id.feedback:
                Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                }
                return true;
            case R.id.profile:
                Intent intent = new Intent(context, MyProfile.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        progressBar.setVisibility(View.INVISIBLE);
        CursorLoader cursorLoader = new CursorLoader(context, QuoteProvider.Quotes.CONTENT_URI, null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        progressBar.setVisibility(View.INVISIBLE);
        int ct = cursor.getCount();
        cursor.moveToLast();
        for (int i = 0; i < ct; i++) {
            Article article = new Article();
            article.setTitle(cursor.getString(cursor.getColumnIndex("newsTitle")));
            article.setDescription(cursor.getString(cursor.getColumnIndex("newsDescription")));
            article.setUrlToImage(cursor.getString(cursor.getColumnIndex("newsImageUrl")));
            newsList.add(article);
            cursor.moveToPrevious();
        }
        Log.e(TAG, newsList.size() + "");
        articleAdapter = new ArticleAdapter(newsList, context);
        newsCardList.setLayoutManager(carouselLayoutManager);
        newsCardList.setHasFixedSize(true);
        newsCardList.setAdapter(articleAdapter);
        articleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}

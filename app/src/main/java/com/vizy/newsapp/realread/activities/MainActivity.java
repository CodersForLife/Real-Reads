package com.vizy.newsapp.realread.activities;

import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.vizy.newsapp.realread.About_Us;
import com.vizy.newsapp.realread.DataBase.QuoteProvider;
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

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String TAG = this.getClass().getSimpleName();
    private String json = "";
    private CarouselLayoutManager carouselLayoutManager;
    private RecyclerView newsCardList;
    private List<Article> newsList;
    private ArticleAdapter articleAdapter;
    private String news;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        newsList = new ArrayList<Article>();
        newsCardList = (RecyclerView) findViewById(R.id.news_list);
        carouselLayoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        carouselLayoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());


     /*   handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {

                articleAdapter = new ArticleAdapter(newsList, MainActivity.this);
                newsCardList.setLayoutManager(carouselLayoutManager);
                newsCardList.setHasFixedSize(true);
                newsCardList.setAdapter(articleAdapter);
                return false;
            }
        });*/


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

                            int presence = 1;
                            JSONObject jsonObject = newsArray.getJSONObject(i);

                            String newsTitle = jsonObject.getString("title");
                            String newsDescription = jsonObject.getString("description");
                            String newsAuthor = jsonObject.getString("author");
                            String newsPublishedAt = jsonObject.getString("publishedAt");
                            String newsUrl = jsonObject.getString("url");
                            String newsUrlToImage = jsonObject.getString("urlToImage");

                         /*   Article article = new Article();
                            article.setAuthor(jsonObject.getString("author"));
                            article.setDescription(jsonObject.getString("description"));
                            article.setPublishedAt(jsonObject.getString("publishedAt"));
                            article.setTitle(jsonObject.getString("title"));
                            article.setUrl(jsonObject.getString("url"));
                            article.setUrlToImage(jsonObject.getString("urlToImage"));
                            newsList.add(article);
                            Log.e("newsList", newsList.size() + "");*/

                            Cursor c=getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,null,null,null,null);

                           do {

                                if (newsTitle.equalsIgnoreCase(c.getString(c.getColumnIndex("newsTitle"))))
                                {
                                    presence = 0;
                                    break;
                                }
                            } while (c.moveToNext());

                            if (presence == 1){
                                ContentValues contentValues=new ContentValues();
                                contentValues.put("newsTitle",newsTitle);
                                contentValues.put("newsDescription",newsDescription);

                                getContentResolver().insert(QuoteProvider.Quotes.CONTENT_URI,contentValues);
                                Log.e(TAG,newsTitle+newsDescription);
                            }
                        }


                    } catch (Exception e) {
                        //    e.printStackTrace();
                    }

                    getLoaderManager().initLoader(0, null, MainActivity.this);

                }
                else {
                    getLoaderManager().initLoader(0, null, MainActivity.this);
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

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        CursorLoader cursorLoader=new CursorLoader(this,QuoteProvider.Quotes.CONTENT_URI,null,null,null,null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int ct=cursor.getCount();
        cursor.moveToFirst();
        for(int i=0;i<ct;i++){
            Article article = new Article();
            article.setTitle(cursor.getString(cursor.getColumnIndex("news_title")));
            article.setDescription(cursor.getString(cursor.getColumnIndex("news_description")));
            newsList.add(article);
            cursor.moveToNext();
        }
        Log.e(TAG,newsList.size()+"");
        articleAdapter = new ArticleAdapter(newsList, MainActivity.this);
        newsCardList.setLayoutManager(carouselLayoutManager);
        newsCardList.setHasFixedSize(true);
        newsCardList.setAdapter(articleAdapter);
        articleAdapter.notifyDataSetChanged();
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}

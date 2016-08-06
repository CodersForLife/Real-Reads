package com.vizy.newsapp.realread.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.vizy.newsapp.realread.R;
import com.vizy.newsapp.realread.adapter.ArticleAdapter;
import com.vizy.newsapp.realread.database.DatabseColumns;
import com.vizy.newsapp.realread.database.QuoteProvider;
import com.vizy.newsapp.realread.model.Article;
import com.vizy.newsapp.realread.ui.customview.CarouselLayoutManager;
import com.vizy.newsapp.realread.ui.customview.CarouselZoomPostLayoutListener;

import java.util.ArrayList;
import java.util.List;

public class Favourites extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public Context context;
    private CarouselLayoutManager carouselLayoutManager;
    private RecyclerView newsCardList;
    private List<Article> newsList;
    private ArticleAdapter articleAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        context = getApplicationContext();

        progressBar = (ProgressBar) findViewById(R.id.favourite_progress_bar);
        newsList = new ArrayList<Article>();
        newsCardList = (RecyclerView) findViewById(R.id.favourite_news_list);
        carouselLayoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        carouselLayoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        progressBar.setVisibility(View.GONE);
        CursorLoader cursorLoader = new CursorLoader(context, QuoteProvider.Quotes.CONTENT_URI, null, DatabseColumns.BOOKMARK
                + " = ?", new String[]{"1"}, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
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

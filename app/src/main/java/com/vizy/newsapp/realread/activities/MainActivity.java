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
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,View.OnClickListener,
View.OnTouchListener{

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
    private Toolbar toolbar;
    private DrawerLayout homeDrawerLayout;
    private NavigationView homeNavigationView;
    private View homeNavigationHeaderView;
    private ImageView userImage;
    private TextView userName,myProfileNavigationDrawer,favouritesNavigationDrawer,aboutUsNavigationDrawer,rateUsNavigationDrawer;
    private LinearLayout logout,myProfile,favouriteLayout,rateUs,aboutUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_main);
//        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(128, 0, 0, 0)));
        context = getApplicationContext();
        homeNavigationView = (NavigationView) findViewById(R.id.nav_view);
        homeNavigationHeaderView = homeNavigationView.getHeaderView(0);
        homeDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      //  toolbar.setTitle("Realreads");
        userImage=(ImageView)homeNavigationHeaderView.findViewById(R.id.user_image);
        logout=(LinearLayout)homeNavigationHeaderView.findViewById(R.id.logout);
        myProfile=(LinearLayout)homeNavigationHeaderView.findViewById(R.id.my_profile);
        favouriteLayout=(LinearLayout)homeNavigationHeaderView.findViewById(R.id.favourite_layout);
        rateUs=(LinearLayout)homeNavigationHeaderView.findViewById(R.id.rate_us);
        aboutUs=(LinearLayout)homeNavigationHeaderView.findViewById(R.id.about_us);
        userName=(TextView)homeNavigationHeaderView.findViewById(R.id.user_name);

        myProfileNavigationDrawer=(TextView)homeNavigationHeaderView.findViewById(R.id.my_profile_navigation_drawer);
        favouritesNavigationDrawer=(TextView)homeNavigationHeaderView.findViewById(R.id.favourites_navigation_drawer);
        aboutUsNavigationDrawer=(TextView)homeNavigationHeaderView.findViewById(R.id.about_us_navigation_drawer);
        rateUsNavigationDrawer=(TextView)homeNavigationHeaderView.findViewById(R.id.rate_us_navigation_drawer);

        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this, homeDrawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                logout.setOnClickListener(MainActivity.this);
                myProfile.setOnClickListener(MainActivity.this);
                favouriteLayout.setOnClickListener(MainActivity.this);
                rateUs.setOnClickListener(MainActivity.this);
                aboutUs.setOnClickListener(MainActivity.this);

                myProfile.setOnTouchListener(MainActivity.this);
                favouriteLayout.setOnTouchListener(MainActivity.this);
                rateUs.setOnTouchListener(MainActivity.this);
                aboutUs.setOnTouchListener(MainActivity.this);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        homeDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.my_profile:
                Intent i=new Intent(MainActivity.this,MyProfile.class);
                startActivity(i);
                break;
            case R.id.favourite_layout:
                Intent j=new Intent(MainActivity.this,Favourites.class);
                startActivity(j);
                break;

            case R.id.rate_us:
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
                break;
            case R.id.about_us:
                Intent k = new Intent(context, About_Us.class);
                startActivity(k);
                break;
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


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
        {
            switch (view.getId())
            {
                case R.id.my_profile:
                    myProfileNavigationDrawer.setTextColor(getResources().getColor(R.color.text_color_navigation_drawer));
                   // myProfile.setBackgroundResource(getResources().getColor(R.color.on_pressed_background_color));
                    myProfile.setBackgroundResource(R.color.on_pressed_background_color);
                    break;

                case R.id.favourite_layout:
                    favouritesNavigationDrawer.setTextColor(getResources().getColor(R.color.text_color_navigation_drawer));
                    favouriteLayout.setBackgroundResource(R.color.on_pressed_background_color);
                    break;

                case R.id.rate_us:
                    rateUsNavigationDrawer.setTextColor(getResources().getColor(R.color.text_color_navigation_drawer));
                    rateUs.setBackgroundResource(R.color.on_pressed_background_color);
                    break;

                case R.id.about_us:
                    aboutUsNavigationDrawer.setTextColor(getResources().getColor(R.color.text_color_navigation_drawer));
                    aboutUs.setBackgroundResource(R.color.on_pressed_background_color);
                    break;

            }
        }
        else if(motionEvent.getAction()==MotionEvent.ACTION_UP)
        {
            switch (view.getId())
            {
                case R.id.my_profile:
                    myProfileNavigationDrawer.setTextColor(getResources().getColor(R.color.on_released_text_color_navigation_drawer));
                    myProfile.setBackgroundResource(R.color.color);
                    break;

                case R.id.favourite_layout:
                    favouritesNavigationDrawer.setTextColor(getResources().getColor(R.color.on_released_text_color_navigation_drawer));
                    favouriteLayout.setBackgroundResource(R.color.color);
                    break;

                case R.id.rate_us:
                    rateUsNavigationDrawer.setTextColor(getResources().getColor(R.color.on_released_text_color_navigation_drawer));
                    rateUs.setBackgroundResource(R.color.color);
                    break;

                case R.id.about_us:
                    aboutUsNavigationDrawer.setTextColor(getResources().getColor(R.color.on_released_text_color_navigation_drawer));
                    aboutUs.setBackgroundResource(R.color.color);
                    break;

            }
        }

        return false;
    }
}

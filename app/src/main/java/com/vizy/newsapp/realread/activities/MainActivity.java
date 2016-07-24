package com.vizy.newsapp.realread.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.vizy.newsapp.realread.About_Us;
import com.vizy.newsapp.realread.Constants;
import com.vizy.newsapp.realread.R;
import com.vizy.newsapp.realread.ui.customview.CarouselLayoutManager;
import com.vizy.newsapp.realread.ui.customview.CarouselZoomPostLayoutListener;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private BottomBar bottomBar;
    private Button share;
    private FloatingActionButton addToBookmark;
    private String json = "";
    private TextView description2, title2;
    private ImageView image2;
    private CarouselLayoutManager carouselLayoutManager;
    private RecyclerView newsCardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsCardList = (RecyclerView) findViewById(R.id.news_list);
        carouselLayoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        carouselLayoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        newsCardList.setLayoutManager(carouselLayoutManager);
        newsCardList.setHasFixedSize(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_us:
                Intent i = new Intent(this, About_Us.class);
                startActivity(i);
                return true;
            case R.id.bookmarks:
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

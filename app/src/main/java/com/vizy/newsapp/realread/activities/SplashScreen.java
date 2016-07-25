package com.vizy.newsapp.realread.activities;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vizy.newsapp.realread.R;

public class SplashScreen extends AppCompatActivity {

    LinearLayout bground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        bground=(LinearLayout) findViewById(R.id.backgound_layout);

        Drawable background=getResources().getDrawable(R.drawable.splash_screen_background);
        background.setAlpha(400);
        bground.setBackground(background);
    }
}

package com.vizy.newsapp.realread.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vizy.newsapp.realread.R;

public class SplashScreen extends AppCompatActivity {

    LinearLayout bground;
    Button createAccount, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        bground=(LinearLayout) findViewById(R.id.backgound_layout);
        createAccount=(Button)findViewById(R.id.create_account);
        login=(Button)findViewById(R.id.login);

        Drawable background=getResources().getDrawable(R.drawable.splash_screen_background);
        background.setAlpha(400);
        bground.setBackground(background);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toCreateAccount=new Intent(SplashScreen.this, SignUp.class);
                startActivity(toCreateAccount);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLogin=new Intent(SplashScreen.this, SignIn.class);
                startActivity(toLogin);
            }
        });
    }
}

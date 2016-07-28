package com.vizy.newsapp.realread.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.vizy.newsapp.realread.R;
import com.vizy.newsapp.realread.model.UserSession;

public class SplashScreen extends AppCompatActivity {


    Button createAccount, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        createAccount = (Button) findViewById(R.id.create_account);
        login = (Button) findViewById(R.id.login);


        UserSession session = new UserSession(getApplicationContext());
        if (session.isLoggedIn()) {
            Intent toHome = new Intent(this, MainActivity.class);
            startActivity(toHome);
            finish();
        }

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toCreateAccount = new Intent(SplashScreen.this, SignUp.class);
                startActivity(toCreateAccount);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLogin = new Intent(SplashScreen.this, SignIn.class);
                startActivity(toLogin);
                finish();
            }
        });
    }


}

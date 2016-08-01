package com.vizy.newsapp.realread.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.vizy.newsapp.realread.R;
import com.vizy.newsapp.realread.model.UserSession;

public class SplashScreen extends AppCompatActivity {

    private UserSession session;
    private Button createAccount, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new UserSession(this);

        setContentView(R.layout.activity_splash_screen);

        createAccount = (Button) findViewById(R.id.create_account);
        login = (Button) findViewById(R.id.login);


        if (session.isLoggedIn()) {
            Intent toHome = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(toHome);
            finish();
        } else {

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

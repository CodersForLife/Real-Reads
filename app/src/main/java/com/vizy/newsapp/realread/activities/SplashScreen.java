package com.vizy.newsapp.realread.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
        //login = (Button) findViewById(R.id.login);



       /* createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               if (session.isLoggedIn()) {
                   Intent toHome = new Intent(SplashScreen.this, MainActivity.class);
                   startActivity(toHome);
                   finish();
               }
               else {
                   Intent toCreateAccount = new Intent(SplashScreen.this, SignIn.class);
                   startActivity(toCreateAccount);
                   finish();
               }

           }
       },2000);
    }


}

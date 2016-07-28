package com.vizy.newsapp.realread.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.vizy.newsapp.realread.R;
import com.vizy.newsapp.realread.model.UserSession;

public class SignUp extends AppCompatActivity {

    EditText mobile, password;
    Button done_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mobile = (EditText) findViewById(R.id.enter_mobile_no);
        password = (EditText) findViewById(R.id.enter_password);
        done_signup = (Button) findViewById(R.id.done_signup);

        done_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = mobile.getText().toString();
                String enterPassword = password.getText().toString();

                UserSession session = new UserSession(getApplicationContext());
                session.createLoginSession(phoneNumber, enterPassword);
                Intent toHome = new Intent(SignUp.this, MainActivity.class);
                startActivity(toHome);
                finish();

            }
        });


    }
}

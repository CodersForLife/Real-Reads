package com.vizy.newsapp.realread.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.vizy.newsapp.realread.R;

public class SignUp extends AppCompatActivity {

    EditText mobile, password, confirm_password;
    Button done_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mobile=(EditText)findViewById(R.id.enter_mobile_no);
        password=(EditText)findViewById(R.id.enter_password);
        confirm_password=(EditText)findViewById(R.id.enter_password_again);
    }
}

package com.vizy.newsapp.realread.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.vizy.newsapp.realread.R;

public class SignIn extends AppCompatActivity {

    Button signIn, noAccount;
    EditText mobileNo, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signIn=(Button)findViewById(R.id.sign_in);
        noAccount=(Button)findViewById(R.id.no_account);
        mobileNo=(EditText)findViewById(R.id.enter_mobile_no_login);
        password=(EditText)findViewById(R.id.enter_password_login);

        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSignUp=new Intent(SignIn.this, SignUp.class);
                startActivity(toSignUp);
            }
        });
        
        googleSignIn();
    }

    private void googleSignIn() {

    }
}

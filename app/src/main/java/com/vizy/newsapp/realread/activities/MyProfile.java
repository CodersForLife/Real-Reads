package com.vizy.newsapp.realread.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.vizy.newsapp.realread.R;
import com.vizy.newsapp.realread.model.UserSession;

import java.util.HashMap;

public class MyProfile extends AppCompatActivity {

    private EditText profileName,profileEmail,profilePhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        profileName=(EditText) findViewById(R.id.profile_name);
        profileEmail=(EditText) findViewById(R.id.profile_email);
        profilePhone=(EditText) findViewById(R.id.profile_number);

        UserSession session = new UserSession(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();
        String name = user.get(UserSession.KEY_NAME);
        String email = user.get(UserSession.KEY_EMAIL);
    }
}

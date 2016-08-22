package com.vizy.newsapp.realread.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vizy.newsapp.realread.R;
import com.vizy.newsapp.realread.model.UserSession;

import java.util.HashMap;

public class MyProfile extends AppCompatActivity {

    private EditText profileName,profileEmail,profilePhone;
    private TextView profileNameTextview,profileEmailTextview,profileNumberTextview;
    private ImageView profilePic;
    private String picUrl,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        //profileName=(EditText) findViewById(R.id.profile_name);
        //profileEmail=(EditText) findViewById(R.id.profile_email);
        //profilePhone=(EditText) findViewById(R.id.profile_number);

        profileEmailTextview=(TextView)findViewById(R.id.profile_email_textview);
        profileNameTextview=(TextView)findViewById(R.id.profile_name_textview);
        //profileNumberTextview=(TextView)findViewById(R.id.profile_phone_textview);

        profilePic=(ImageView) findViewById(R.id.profile_pic);

        UserSession session = new UserSession(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();

        if(user.get(UserSession.GOOGLE_ID)==null)
        {
            id=user.get(UserSession.KEY_ID);
            picUrl="https://graph.facebook.com/" + id + "/picture?type=large";
        }
        else
        {
           //id=user.get(UserSession.GOOGLE_ID);
            //picUrl="https://www.googleapis.com/plus/v1/people/"+id+"?sz=100";
            picUrl=user.get(UserSession.GOOGLE_ID);

        }

        Log.e("url",picUrl);
        String name = user.get(UserSession.KEY_NAME);
        String email = user.get(UserSession.KEY_EMAIL);




        if(name.length()>0)
        {
            profileNameTextview.setVisibility(View.VISIBLE);
            //profileName.setVisibility(View.GONE);
            profileNameTextview.setText(name);
        }
        if(email.length()>0){
            profileEmailTextview.setVisibility(View.VISIBLE);
            //profileEmail.setVisibility(View.GONE);
            profileEmailTextview.setText(email);
        }

        Picasso.with(this).load(picUrl).into(profilePic);
    }
}

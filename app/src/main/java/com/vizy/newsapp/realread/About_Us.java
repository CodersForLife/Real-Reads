package com.vizy.newsapp.realread;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class About_Us extends AppCompatActivity implements View.OnClickListener {

    String url_to_be_opened="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        ImageButton aditya,tanveer,shanu,archit,nimit,piyush,saad,kaabir,tanveer_mobile;
        ImageView adi,tanv,shan,arc,nimmo,piy,saa,kaabu;

        adi=(ImageView)findViewById(R.id.aditya_image);
        tanv=(ImageView)findViewById(R.id.tanveer_image);
        shan=(ImageView)findViewById(R.id.shanu_image);
        arc=(ImageView)findViewById(R.id.archit_image);
        nimmo=(ImageView)findViewById(R.id.nimit_image);
        piy=(ImageView)findViewById(R.id.piyush_image);
        saa=(ImageView)findViewById(R.id.saad_image);
        kaabu=(ImageView)findViewById(R.id.kabir_image);

        adi.setImageResource(R.drawable.p1);
        tanv.setImageResource(R.drawable.p2);
        shan.setImageResource(R.drawable.p3);
        piy.setImageResource(R.drawable.p4);
        nimmo.setImageResource(R.drawable.p5);
        kaabu.setImageResource(R.drawable.p6);
        arc.setImageResource(R.drawable.p8);
        saa.setImageResource(R.drawable.p7);

        aditya=(ImageButton)findViewById(R.id.aditya_link);
        tanveer=(ImageButton)findViewById(R.id.tanveer_link);
        shanu=(ImageButton)findViewById(R.id.shanu_link);
        archit=(ImageButton)findViewById(R.id.archit_link);
        nimit=(ImageButton)findViewById(R.id.nimit_link);
        piyush=(ImageButton)findViewById(R.id.piyush_link);
        saad=(ImageButton)findViewById(R.id.saad_link);
        kaabir=(ImageButton)findViewById(R.id.kabir_link);
        tanveer_mobile=(ImageButton)findViewById(R.id.tanveer_phone);

        aditya.setOnClickListener(this);
        tanveer.setOnClickListener(this);
        shanu.setOnClickListener(this);
        archit.setOnClickListener(this);
        nimit.setOnClickListener(this);
        piyush.setOnClickListener(this);
        saad.setOnClickListener(this);
        kaabir.setOnClickListener(this);
        tanveer_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:8527862446"));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.aditya_link:url_to_be_opened="https://github.com/AadityaDev";break;
            case R.id.tanveer_link:url_to_be_opened="https://www.linkedin.com/in/tanveersid01";break;
            case R.id.archit_link:url_to_be_opened="https://www.linkedin.com/in/archit-gupta-043b0329?trk=nav_responsive_tab_profile_pic"
            ;break;
            case R.id.shanu_link:url_to_be_opened="https://www.linkedin.com/profile/view?id=424674153";break;
            case R.id.nimit_link:url_to_be_opened="https://github.com/nimit95";break;
            case R.id.saad_link:url_to_be_opened="https://github.com/syedsaadh/";break;
            case R.id.kabir_link:url_to_be_opened="https://www.behance.net/kabbu1";break;
            case R.id.piyush_link:url_to_be_opened="https://www.linkedin.com/in/piyush6348";break;

        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url_to_be_opened));
        startActivity(browserIntent);

    }
}

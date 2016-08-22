package com.vizy.newsapp.realread;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class About_Us extends AppCompatActivity implements View.OnClickListener {

    private String url_to_be_opened = "";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        ImageButton aditya, tanveer, archit, nimit, piyush, saad, kaabir, tanveer_mobile;
        ImageView adi, tanv, arc, nimmo, piy, saa, kaabu;

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        this.setTitle("About Us");

        adi = (ImageView) findViewById(R.id.aditya_image);
        tanv = (ImageView) findViewById(R.id.tanveer_image);
        arc = (ImageView) findViewById(R.id.archit_image);
        nimmo = (ImageView) findViewById(R.id.nimit_image);
        piy = (ImageView) findViewById(R.id.piyush_image);
        saa = (ImageView) findViewById(R.id.saad_image);
        kaabu = (ImageView) findViewById(R.id.kabir_image);


        Picasso.with(this).load(Constants.aditya_image_url).transform(new CircleTransform()).into(adi);
        Picasso.with(this).load(Constants.piyush_image_url).transform(new CircleTransform()).into(piy);
        Picasso.with(this).load(Constants.archit_image_url).transform(new CircleTransform()).into(arc);
        Picasso.with(this).load(Constants.tanveer_image_url).transform(new CircleTransform()).into(tanv);
        Picasso.with(this).load(Constants.nimit_image_url).transform(new CircleTransform()).into(nimmo);
        Picasso.with(this).load(Constants.saad_image_url).transform(new CircleTransform()).into(saa);
        Picasso.with(this).load(Constants.kaabir_image_url).transform(new CircleTransform()).into(kaabu);

        aditya = (ImageButton) findViewById(R.id.aditya_link);
        tanveer = (ImageButton) findViewById(R.id.tanveer_link);
        archit = (ImageButton) findViewById(R.id.archit_link);
        nimit = (ImageButton) findViewById(R.id.nimit_link);
        piyush = (ImageButton) findViewById(R.id.piyush_link);
        saad = (ImageButton) findViewById(R.id.saad_link);
        kaabir = (ImageButton) findViewById(R.id.kabir_link);
        tanveer_mobile = (ImageButton) findViewById(R.id.tanveer_phone);

        aditya.setOnClickListener(this);
        tanveer.setOnClickListener(this);
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
        int id = view.getId();
        switch (id) {
            case R.id.aditya_link:
                url_to_be_opened = "https://github.com/AadityaDev";
                break;
            case R.id.tanveer_link:
                url_to_be_opened = "https://www.linkedin.com/in/tanveersid01";
                break;
            case R.id.archit_link:
                url_to_be_opened = "https://www.linkedin.com/in/archit-gupta-043b0329?trk=nav_responsive_tab_profile_pic";
                break;
            case R.id.nimit_link:
                url_to_be_opened = "https://github.com/nimit95";
                break;
            case R.id.saad_link:
                url_to_be_opened = "https://github.com/syedsaadh/";
                break;
            case R.id.kabir_link:
                url_to_be_opened = "https://www.behance.net/kabbu1";
                break;
            case R.id.piyush_link:
                url_to_be_opened = "https://www.linkedin.com/in/piyush6348";
                break;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url_to_be_opened));
        startActivity(browserIntent);

    }

    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}

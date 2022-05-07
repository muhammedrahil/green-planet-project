package com.example.greenplanet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class View_class_details extends AppCompatActivity {


    TextView l1,l2,l3,l4,l6,l7;
    ImageView l5;
    String im;

    SharedPreferences sp;
    String url="",ip="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_class_details);

        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        l1=findViewById(R.id.textView85);
        l2=findViewById(R.id.textView87);
        l3=findViewById(R.id.textView86);
        l4=findViewById(R.id.textView89);
        l5=findViewById(R.id.imageView2);
        l6=findViewById(R.id.textView92);
        l7=findViewById(R.id.textView94);
        l1.setText(getIntent().getStringExtra("name"));
        l2.setText(getIntent().getStringExtra("Phone"));
        l3.setText(getIntent().getStringExtra("Date"));
        l4.setText(getIntent().getStringExtra("Time"));
        l6.setText(getIntent().getStringExtra("Location"));
        l7.setText(getIntent().getStringExtra("Type"));
        im=getIntent().getStringExtra("Photo");


        if(android.os.Build.VERSION.SDK_INT>9)
        {
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

//        i2.setImageDrawable(Drawable.createFromPath(getIntent().getStringExtra("photo"))));
        java.net.URL thumb_u;
        try {

            //thumb_u = new java.net.URL("http://192.168.43.57:5000/static/photo/flyer.jpg");

            thumb_u = new java.net.URL("http://"+sp.getString("ip","")+":5000/static/files/"+im);
            Drawable thumb_d = Drawable.createFromStream(thumb_u.openStream(), "src");
            l5.setImageDrawable(thumb_d);

        }
        catch (Exception e)
        {
            Log.d("errsssssssssssss",""+e);
        }

    }
}
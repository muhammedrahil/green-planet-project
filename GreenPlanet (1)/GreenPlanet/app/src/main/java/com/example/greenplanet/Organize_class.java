package com.example.greenplanet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Organize_class extends AppCompatActivity {

    TextView name,phone,date,location;
    ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organize_class);

        name=findViewById(R.id.textView56);
        phone=findViewById(R.id.textView58);
        date=findViewById(R.id.textView60);
        location=findViewById(R.id.textView62);
        photo=findViewById(R.id.imageView);
    }
}
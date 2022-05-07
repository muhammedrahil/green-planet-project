package com.example.greenplanet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Organize_training extends AppCompatActivity {

    TextView name,phone,date,location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organize_training);

        name=findViewById(R.id.textView48);
        phone=findViewById(R.id.textView50);
        date=findViewById(R.id.textView52);
        location=findViewById(R.id.textView54);
    }

}
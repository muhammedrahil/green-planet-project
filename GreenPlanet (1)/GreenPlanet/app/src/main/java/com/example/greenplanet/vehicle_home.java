package com.example.greenplanet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class vehicle_home extends AppCompatActivity {

    Button request,notifications,route,bill,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_home);


        request=findViewById(R.id.button21);
        notifications=findViewById(R.id.button23);
        route=findViewById(R.id.button24);

        logout=findViewById(R.id.button26);

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(getApplicationContext(),View_user_request.class);
                startActivity(i);

            }

        });

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(getApplicationContext(),pricechart.class);
                startActivity(i);

            }
        });

        route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(getApplicationContext(),View_route.class);
                startActivity(i);

            }
        });




        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(getApplicationContext(),login.class);
                startActivity(i);


            }
        });
    }
}
package com.example.greenplanet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class View_pickup_point extends AppCompatActivity {

    ListView l6;

    SharedPreferences sp;
    String url="",ip="";
    ArrayList<String> Date,Latitude,Longitude,Location,Vehicle_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pickup_point);

        l6=findViewById(R.id.list4);





        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        url ="http://"+sp.getString("ip", "") + ":5000/view_pickup_point";

        RequestQueue queue = Volley.newRequestQueue(View_pickup_point.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);

                    Date= new ArrayList<>();
                    Latitude= new ArrayList<>();
                    Longitude= new ArrayList<>();
                    Location= new ArrayList<>();
                    Vehicle_no= new ArrayList<>();



                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);

                        Date.add(jo.getString("Date"));
                        Latitude.add(jo.getString("Latitude"));
                        Longitude.add(jo.getString("Longitude"));
                        Location.add(jo.getString("Location"));
                        Vehicle_no.add(jo.getString("Vehicle_no"));






                    }

                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    l6.setAdapter(new custom3(View_pickup_point.this,Date,Location,Vehicle_no));
//                    l6.setOnItemClickListener(view_complaint.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(View_pickup_point.this, "err"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();



                return params;
            }
        };
        queue.add(stringRequest);
    }
}
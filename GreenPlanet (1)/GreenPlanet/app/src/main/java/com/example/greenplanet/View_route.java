package com.example.greenplanet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class View_route extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView l8;


    SharedPreferences sp;
    String url="",ip="";
    ArrayList<String> Date,Latitude,Longitude,Location;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_route);

        l8=findViewById(R.id.list6);


        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        url ="http://"+sp.getString("ip", "") + ":5000/view_route";

        RequestQueue queue = Volley.newRequestQueue(View_route.this);

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



                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);

                        Date.add(jo.getString("Date"));
                        Latitude.add(jo.getString("Latitude"));
                        Longitude.add(jo.getString("Longitude"));
                        Location.add(jo.getString("Location"));


                    }

                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    l8.setAdapter(new custom2(View_route.this,Date,Location));
                    l8.setOnItemClickListener(View_route.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(View_route.this, "err"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("lid",sp.getString("lid",""));



                return params;
            }
        };
        queue.add(stringRequest);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i =new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://maps.google.com/maps?q="+Latitude.get(position)+","+Longitude.get(position)));
        startActivity(i);
    }
}
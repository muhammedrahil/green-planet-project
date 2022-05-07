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

public class my_request_status extends AppCompatActivity {
    ListView l9;

    SharedPreferences sp;
    String url="",ip="";
    ArrayList<String> Waste_type,Quantity,Status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_request_status);
        l9=findViewById(R.id.list7);


        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        url ="http://"+sp.getString("ip", "") + ":5000/View_my_request";

        RequestQueue queue = Volley.newRequestQueue(my_request_status.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);


                    Waste_type= new ArrayList<>();
                    Quantity= new ArrayList<>();

                    Status= new ArrayList<>();




                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);



                        Waste_type.add(jo.getString("waste_type"));
                        Quantity.add(jo.getString("quantity"));

                        Status.add(jo.getString("Status"));







                    }

                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    l9.setAdapter(new custom3(my_request_status.this,Waste_type,Quantity,Status));
//                    l9.setOnItemClickListener(view_complaint.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(my_request_status.this, "err"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id",sp.getString("lid", ""));




                return params;
            }
        };
        queue.add(stringRequest);
    }
}
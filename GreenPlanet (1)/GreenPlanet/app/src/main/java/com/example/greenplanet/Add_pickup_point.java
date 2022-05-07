package com.example.greenplanet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class Add_pickup_point extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner Area,s1;
    EditText Waste_type,quantity;
Button Add;
String wast_type,Quantity,aid="";
    ArrayList<String> aaid,place;

    String url="",url2;
    String ip="";
    SharedPreferences sh;
String wtype[]={"recycle waste","non recycle waste"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pickup_point);

        s1=findViewById(R.id.spinner2);

        Add=findViewById(R.id.button7);
        Area=findViewById(R.id.spinner);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ip=sh.getString("ip","");


        url="http://"+ip+":5000/view_pickup_point";

        ArrayAdapter<String> ad1=new ArrayAdapter<>(Add_pickup_point.this,android.R.layout.simple_list_item_1,wtype);
        //lv.setAdapter(ad);
        s1.setAdapter(ad1);


        RequestQueue queue = Volley.newRequestQueue(Add_pickup_point.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);

                    aaid= new ArrayList<>();
                    place= new ArrayList<>();




                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);

                        aaid.add(jo.getString("Area_id"));

                        place.add(jo.getString("Location"));


                    }

                     ArrayAdapter<String> ad=new ArrayAdapter<>(Add_pickup_point.this,android.R.layout.simple_list_item_1,place);
                    //lv.setAdapter(ad);
                    Area.setAdapter(ad);
                    Area.setOnItemSelectedListener(Add_pickup_point.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(Add_pickup_point.this, "err"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();




                return params;
            }
        };
        queue.add(stringRequest);





        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ip=sh.getString("ip","");





        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url2="http://"+ip+":5000/send_request";

                wast_type=s1.getSelectedItem().toString();


                if(wast_type.equalsIgnoreCase(""))
                {
                    Waste_type.setError("Enter your Wast Type");
                }



                 else {


                    // Instantiate the RequestQueue.
                    RequestQueue queue = Volley.newRequestQueue(Add_pickup_point.this);

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the response string.

                            try {
                                JSONObject jo = new JSONObject(response);
                                String status = jo.getString("task");

                                if (status.equalsIgnoreCase("success")) {
                                    Toast.makeText(getApplicationContext(), "Pickup Point Added ", Toast.LENGTH_SHORT).show();
//
//
                                    Intent i = new Intent(getApplicationContext(), Customer_home.class);
                                    startActivity(i);

                                } else {
                                    Toast.makeText(Add_pickup_point.this, "Not Added", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {

                                Toast.makeText(Add_pickup_point.this, "" + e, Toast.LENGTH_SHORT).show();

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("Customer_id", sh.getString("lid", ""));
                            params.put("aid", aid);
                            params.put("waste_type", wast_type);

                            params.put("lati", LocactionService.lati);
                            params.put("longi", LocactionService.logi);

                            return params;
                        }
                    };
                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       aid=aaid.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
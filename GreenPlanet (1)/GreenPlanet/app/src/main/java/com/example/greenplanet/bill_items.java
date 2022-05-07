package com.example.greenplanet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
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

public class bill_items extends AppCompatActivity {

    EditText amount;
    RadioButton d1,d2;
    Button send;
    ListView l3;



    String url="",status;
    String ip="";
    SharedPreferences sh;

    ArrayList<String> productname,price,date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_items);


        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ip=sh.getString("ip","");

        url="http://"+ip+":5000/view_product_bill";



        d1=findViewById(R.id.radioButton5);
        d2=findViewById(R.id.radioButton6);
        send=findViewById(R.id.button9);



        l3=findViewById(R.id.list);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        url ="http://"+sh.getString("ip", "") + ":5000/viewallls";

        RequestQueue queue = Volley.newRequestQueue(bill_items.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);


                    productname= new ArrayList<>();


                    price= new ArrayList<>();


                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);

                        productname.add(jo.getString("Product_name"));
                        price.add(jo.getString("Price"));



                    }

                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    l3.setAdapter(new custom2(bill_items.this,productname,price));


                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(bill_items.this, "err"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("bid", sh.getString("billid",""));



                return params;
            }
        };
        queue.add(stringRequest);




        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (d1.isChecked())
                {
                    status=d1.getText().toString();
                }
                else
                    if (d2.isChecked())
                    {
                        status=d2.getText().toString();
                    }
                url ="http://"+sh.getString("ip", "") + ":5000/update_status";


                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(bill_items.this);


                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.

                        try {
                            JSONObject jo = new JSONObject(response);
                            String status = jo.getString("task");

                            if (status.equalsIgnoreCase("success")) {
                                Toast.makeText(getApplicationContext(), "Send ", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(getApplicationContext(), vehicle_home.class);
                                startActivity(i);
//
//


                            } else {
                                Toast.makeText(bill_items.this, "Not Send", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {

                            Toast.makeText(bill_items.this, "" + e, Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(bill_items.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("bid", sh.getString("billid",""));
                        params.put("status",status);








                        return params;
                    }
                };
                // Add the request to the RequestQueue.
                queue.add(stringRequest);





            }
        });


    }
}
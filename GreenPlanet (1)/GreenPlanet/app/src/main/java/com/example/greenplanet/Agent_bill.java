package com.example.greenplanet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class Agent_bill extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView l3;

    SharedPreferences sp;
    String url="",ip="";
    ArrayList<String> Bill_id,Customer_name,amount,date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_bill);


        l3=findViewById(R.id.list1);

        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        url ="http://"+sp.getString("ip", "") + ":5000/viewalll";

        RequestQueue queue = Volley.newRequestQueue(Agent_bill.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);

                    Bill_id= new ArrayList<>();
                    Customer_name= new ArrayList<>();
                    amount=new ArrayList<>();
                    date=new ArrayList<>();



                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);

                        Bill_id.add(jo.getString("billid"));
                        Customer_name.add(jo.getString("Fname")+" "+(jo.getString("Lname")));
                        date.add(jo.getString("date"));
                        amount.add(jo.getString("amount"));


                    }

                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    l3.setAdapter(new custom4(Agent_bill.this,Bill_id,Customer_name,date,amount));
                    l3.setOnItemClickListener(Agent_bill.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(Agent_bill.this, "err"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Vid",sp.getString("lid",""));



                return params;
            }
        };
        queue.add(stringRequest);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        SharedPreferences.Editor ed=sp.edit();
        ed.putString("billid",Bill_id.get(position));
        ed.commit();
        startActivity(new Intent(getApplicationContext(),bill_items.class));







    }
}
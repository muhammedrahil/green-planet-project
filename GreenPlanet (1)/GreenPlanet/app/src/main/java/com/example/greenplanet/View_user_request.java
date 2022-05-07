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

public class View_user_request extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView l9;

    SharedPreferences sp;
    String url="",ip="";
    ArrayList<String> name,lati,longi,Phone,Request_id,customer_id,Assignarea_id,Waste_type,Quantity,Date,Status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_request);

        l9=findViewById(R.id.list7);


        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        url ="http://"+sp.getString("ip", "") + ":5000/view_user_resquest";

        RequestQueue queue = Volley.newRequestQueue(View_user_request.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);

                    name= new ArrayList<>();
                    Phone= new ArrayList<>();
                    Request_id= new ArrayList<>();
                    customer_id= new ArrayList<>();
                    Assignarea_id= new ArrayList<>();
                    Waste_type= new ArrayList<>();
                    Quantity= new ArrayList<>();
                    Date= new ArrayList<>();
                    Status= new ArrayList<>();

                    lati= new ArrayList<>();
                    longi= new ArrayList<>();



                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);

                        name.add(jo.getString("Fname")+" "+jo.getString("Lname"));
                        Phone.add(jo.getString("Phone"));
                        Request_id.add(jo.getString("Request_id"));
                        customer_id.add(jo.getString("Customer_id"));
                        Assignarea_id.add(jo.getString("Assignarea_id"));
                        Waste_type.add(jo.getString("waste_type"));
                        Quantity.add(jo.getString("quantity"));
                        Date.add(jo.getString("Date"));
                        Status.add(jo.getString("Status"));



                        lati.add(jo.getString("lati"));
                        longi.add(jo.getString("longi"));




                    }

                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    l9.setAdapter(new custom3(View_user_request.this,name,Waste_type,Quantity));
                    l9.setOnItemClickListener(View_user_request.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(View_user_request.this, "err"+error, Toast.LENGTH_SHORT).show();
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


        Intent i=new Intent(getApplicationContext(),Cash_entry.class);
        i.putExtra("name",name.get(position));
        i.putExtra("wast",Waste_type.get(position));
        i.putExtra("lati",lati.get(position));
        i.putExtra("longi",longi.get(position));

        i.putExtra("request_id",Request_id.get(position));
        i.putExtra("userid",customer_id.get(position));
        startActivity(i);



    }
}
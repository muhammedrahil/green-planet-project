package com.example.greenplanet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class View_work_details extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView l10;

    SharedPreferences sp;
    String url="",ip="";
    ArrayList<String> work,description,wid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewclass_and_viewtraining);

        l10=findViewById(R.id.list8);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        url ="http://"+sp.getString("ip", "") + ":5000/viewworkk";

        RequestQueue queue = Volley.newRequestQueue(View_work_details.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);

                    work= new ArrayList<>();
                    description= new ArrayList<>();

                    wid= new ArrayList<>();



                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);
                        work.add(jo.getString("work"));
                        wid.add(jo.getString("id"));

                        description.add(jo.getString("description"));






                    }

                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    l10.setAdapter(new custom2(View_work_details.this,work,description));
                    l10.setOnItemClickListener(View_work_details.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(View_work_details.this, "err"+error, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        url ="http://"+sp.getString("ip", "") + ":5000/requestwork";

        AlertDialog.Builder ald=new AlertDialog.Builder(View_work_details.this);
        ald.setTitle("Do you want to request for work")
                .setPositiveButton(" YES ", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                        // Instantiate the RequestQueue.
                        RequestQueue queue = Volley.newRequestQueue(View_work_details.this);

                        // Request a string response from the provided URL.
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the response string.

                                try {
                                    JSONObject jo = new JSONObject(response);
                                    String status = jo.getString("task");

                                    if (status.equalsIgnoreCase("success")) {
                                        Toast.makeText(View_work_details.this, "success", Toast.LENGTH_SHORT).show();


startActivity(new Intent(getApplicationContext(),Customer_home.class));



                                    } else
                                    {
                                        Toast.makeText(View_work_details.this, "error", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {

                                    Toast.makeText(View_work_details.this, ""+e, Toast.LENGTH_SHORT).show();

                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(View_work_details.this, error+"errrrrrrr", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("uid",sp.getString("lid",""));
                                params.put("wid", wid.get(i));

                                return params;
                            }
                        };
                        // Add the request to the RequestQueue.
                        queue.add(stringRequest);
                    }
                })
                .setNegativeButton(" NO ", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
startActivity(new Intent(getApplicationContext(),Customer_home.class));
                    }
                });

        AlertDialog al=ald.create();
        al.show();

    }
}
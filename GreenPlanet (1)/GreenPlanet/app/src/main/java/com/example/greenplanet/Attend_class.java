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

public class Attend_class extends AppCompatActivity {

    ListView l1;

    SharedPreferences sp;
    String url="",ip="";
    ArrayList<String> Name,Phone,Date,Time,Photo,Location,Type;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_class);

        l1=findViewById(R.id.list);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        url ="http://"+sp.getString("ip", "") + ":5000/attend_training_class";

        RequestQueue queue = Volley.newRequestQueue(Attend_class.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);

                    Name= new ArrayList<>();
                    Phone= new ArrayList<>();
                    Date=new ArrayList<>();
                    Time=new ArrayList<>();
                    Photo=new ArrayList<>();
                    Location=new ArrayList<>();
                    Type=new ArrayList<>();




                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);

                        Name.add(jo.getString("Name")+"\n"+jo.getString("Phone")+"\n"+jo.getString("Date")+""+jo.getString("Time"));
                        Phone.add(jo.getString("Phone"));
                        Date.add(jo.getString("Date"));
                        Time.add(jo.getString("Time"));
                        Photo.add(jo.getString("Photo"));
                        Location.add(jo.getString("Location"));
                        Type.add(jo.getString("Type"));


                    }

                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    l1.setAdapter(new custom4(Attend_class.this,Name,Photo,Location,Type));
//                    l1.setOnItemClickListener(view_complaint.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(Attend_class.this, "err"+error, Toast.LENGTH_SHORT).show();
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
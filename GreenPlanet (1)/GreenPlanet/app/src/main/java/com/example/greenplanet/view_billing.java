package com.example.greenplanet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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

public class view_billing extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ListView l3;
    Spinner s3;

    SharedPreferences sp;
    String url="",ip="";
    ArrayList<String> bid,Amount,Wast_Type,Status;
    String wtype[]={"recycle waste","non recycle waste"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_billing);

        l3=findViewById(R.id.list1);
        s3=findViewById(R.id.spinner3);

        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ArrayAdapter<String> ad1=new ArrayAdapter<>(view_billing.this,android.R.layout.simple_list_item_1,wtype);

        s3.setAdapter(ad1);
        s3.setOnItemSelectedListener(view_billing.this);



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



        url ="http://"+sp.getString("ip", "") + ":5000/view_waste_bill";

        RequestQueue queue = Volley.newRequestQueue(view_billing.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);

                    bid= new ArrayList<>();
                    Amount= new ArrayList<>();
                    Status=new ArrayList<>();


                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);

                        bid.add(jo.getString("Bill_id")+"\n"+jo.getString("Date"));
                        Amount.add(jo.getString("Amount"));
                        Status.add(jo.getString("Status"));


                    }

                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    l3.setAdapter(new custom3(view_billing.this,bid,Amount,Status));
//                    l3.setOnItemClickListener(view_complaint.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(view_billing.this, "err"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("uid",sp.getString("lid",""));

                params.put("wtype",s3.getSelectedItem().toString());


                return params;
            }
        };
        queue.add(stringRequest);





    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
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

public class View_product extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView l7;

    SharedPreferences sp;
    String url="",ip="";
    ArrayList<String> Product_name,Details,Image,Price,Product_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        l7=findViewById(R.id.list5);



        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        url ="http://"+sp.getString("ip", "") + ":5000/view_product";

        RequestQueue queue = Volley.newRequestQueue(View_product.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);

                    Product_name= new ArrayList<>();
                    Details= new ArrayList<>();
                    Image= new ArrayList<>();
                    Price= new ArrayList<>();
                    Product_id= new ArrayList<>();




                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);

                        Product_name.add(jo.getString("Product_name"));
                        Details.add(jo.getString("Details"));
                        Image.add(jo.getString("Image"));
                        Price.add(jo.getString("Price"));
                        Product_id.add(jo.getString("Product_id"));







                    }

                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    l7.setAdapter(new custom3(View_product.this,Product_name,Details,Price));
                    l7.setOnItemClickListener(View_product.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(View_product.this, "err"+error, Toast.LENGTH_SHORT).show();
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
        Intent i1=new Intent(getApplicationContext(),Order_Product.class);
        i1.putExtra("Product_name",Product_name.get(i));
        i1.putExtra("Price",Price.get(i));
        i1.putExtra("Product_id",Product_id.get(i));
        i1.putExtra("image",Image.get(i));

        startActivity(i1);


    }
}
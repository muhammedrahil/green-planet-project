package com.example.greenplanet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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
import java.util.Locale;
import java.util.Map;

public class Cash_entry extends AppCompatActivity {
    TextView Customer,waste_type,t43;
    EditText  Amount;
    Button submit;
    RadioButton pending,paid;
    EditText Quantity,t42;

    String sts,Amount1,customer_id,request_id,qty,lati,longi;

    SharedPreferences sp;
    String url="",ip="";
    ArrayList<String> status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_entry);

        Customer=findViewById(R.id.textView44);
        waste_type=findViewById(R.id.textView45);
        Quantity=findViewById(R.id.textView);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        submit=findViewById(R.id.button3);
        ip=sp.getString("ip","");

        t42=findViewById(R.id.textView42);

        Amount=findViewById(R.id.amount);


        t43=findViewById(R.id.textView43);

        Customer.setText(getIntent().getStringExtra("name"));
        waste_type.setText(getIntent().getStringExtra("wast"));
        Quantity.setText(getIntent().getStringExtra("qty"));
        request_id=getIntent().getStringExtra("request_id");
        customer_id=getIntent().getStringExtra("userid");

        lati=getIntent().getStringExtra("lati");
        longi=getIntent().getStringExtra("longi");


        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ip=sp.getString("ip","");

        url="http://"+ip+":5000/cash_entry";
        t43.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://maps.google.com/maps?q="+lati+","+longi));
                startActivity(i);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Amount1=Amount.getText().toString();
                qty=Quantity.getText().toString();
                sts=t42.getText().toString();




                if(Amount1.equalsIgnoreCase(""))
                {
                    Amount.setError("Enter your Amount");
                    Amount.requestFocus();
                }
           else  if(qty.equalsIgnoreCase(""))
                {
                    Quantity.setError("Enter your Quantity");
                    Quantity.requestFocus();
                }

                else  if(sts.equalsIgnoreCase(""))
                {
                    t42.setError("Enter your status");
                    t42.requestFocus();
                }
                else {


                    // Instantiate the RequestQueue.
                    RequestQueue queue = Volley.newRequestQueue(Cash_entry.this);


                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the response string.

                            try {
                                JSONObject jo = new JSONObject(response);
                                String status = jo.getString("task");

                                if (status.equalsIgnoreCase("success")) {
                                    Toast.makeText(getApplicationContext(), "Success ", Toast.LENGTH_SHORT).show();

                                    Intent i = new Intent(getApplicationContext(), vehicle_home.class);
                                    startActivity(i);
//
//


                                } else {
                                    Toast.makeText(Cash_entry.this, "Not Submit", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {

                                Toast.makeText(Cash_entry.this, "" + e, Toast.LENGTH_SHORT).show();

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Cash_entry.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("Customer_id", customer_id);
                            params.put("status", sts);
                            params.put("Amount", Amount1);
                            params.put("Request_id", request_id);
                            params.put("qty", qty);


                            return params;
                        }
                    };
                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);
                }



            }
        });

    }
}
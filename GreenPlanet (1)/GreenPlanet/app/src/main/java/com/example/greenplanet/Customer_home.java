package com.example.greenplanet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Customer_home extends AppCompatActivity {

    Button pickuppoints,b23, billings ,classes, product, work, logout, Status, product_bill,b20,b21,b22;
    SharedPreferences sh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        pickuppoints = findViewById(R.id.button8);
        billings = findViewById(R.id.button10);
        b21 = findViewById(R.id.button21);
        b22 = findViewById(R.id.button22);

        classes = findViewById(R.id.button12);
        product = findViewById(R.id.button13);
        work = findViewById(R.id.button14);
        logout = findViewById(R.id.button15);
        Status = findViewById(R.id.button16);
        product_bill = findViewById(R.id.button19);
        b20 = findViewById(R.id.button20);
        b23 = findViewById(R.id.button23);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        b20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), pricechart.class);
                startActivity(i);
            }
        });

        pickuppoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Add_pickup_point.class);
                startActivity(i);

            }
        });

        billings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), view_billing.class);
                startActivity(i);
            }
        });

        classes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), View_notification.class);
                startActivity(i);
            }
        });
        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                RequestQueue queue = Volley.newRequestQueue(Customer_home.this);
           String     url = "http://" +sh.getString("ip", "") + ":5000/purstart";

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        Log.d("+++++++++++++++++", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            String res = json.getString("task");

                            if (res.equalsIgnoreCase("invalid")) {

                                Toast.makeText(Customer_home.this, "invalid", Toast.LENGTH_SHORT).show();

                            } else {
                                SharedPreferences.Editor edp = sh.edit();
                                edp.putString("billid", res);
                                edp.commit();


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("uid", sh.getString("lid","") );

                        return params;
                    }
                };
                queue.add(stringRequest);



                Intent i = new Intent(getApplicationContext(), View_product.class);
                startActivity(i);
            }
        });
        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), View_work_details.class);
                startActivity(i);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), login.class);
                startActivity(i);
            }
        });
        b23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), add_feedback.class);
                startActivity(i);
            }
        });
        Status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), View_user_request_work.class);
                startActivity(i);
            }
        });

        product_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), product_bill.class);
                startActivity(i);
            }
        });
        b21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), add_complaint.class);
                startActivity(i);
            }
        });
        b22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), view_reply.class);
                startActivity(i);
            }
        });

    }
}
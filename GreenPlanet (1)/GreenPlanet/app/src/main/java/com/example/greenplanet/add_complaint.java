package com.example.greenplanet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class add_complaint extends AppCompatActivity {

    Button submit;
    EditText comp;
    String complaint;

    String url="";
    String ip="";
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_complaint);

        submit=findViewById(R.id.button6);
        comp=findViewById(R.id.uname);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ip=sh.getString("ip","");

        url="http://"+ip+":5000/add_complaint";


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                complaint=comp.getText().toString();

                if(complaint.equalsIgnoreCase(""))
                {
                    comp.setError("Enter your Complaint");
                    comp.requestFocus();
                }
                else {


                    // Instantiate the RequestQueue.
                    RequestQueue queue = Volley.newRequestQueue(add_complaint.this);

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the response string.

                            try {
                                JSONObject jo = new JSONObject(response);
                                String status = jo.getString("task");

                                if (status.equalsIgnoreCase("success")) {
                                    Toast.makeText(getApplicationContext(), "Complaint Send ", Toast.LENGTH_SHORT).show();

                                    Intent i = new Intent(getApplicationContext(), Customer_home.class);
                                    startActivity(i);
//
//


                                } else {
                                    Toast.makeText(add_complaint.this, "Not Send", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {

                                Toast.makeText(add_complaint.this, "" + e, Toast.LENGTH_SHORT).show();

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(add_complaint.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("Customer_id", sh.getString("lid", ""));
                            params.put("Complaint", complaint);


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
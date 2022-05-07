package com.example.greenplanet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class add_feedback extends AppCompatActivity {

    Button submit;
    EditText comp;
    String fd;

    String url="";
    String ip="";
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feedback);

        submit=findViewById(R.id.button6);
        comp=findViewById(R.id.uname);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ip=sh.getString("ip","");

        url="http://"+ip+":5000/feedback";


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fd=comp.getText().toString();

                if(fd.equalsIgnoreCase(""))
                {
                    comp.setError("Enter your feedback");
                    comp.requestFocus();
                }
                else {


                    // Instantiate the RequestQueue.
                    RequestQueue queue = Volley.newRequestQueue(add_feedback.this);

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the response string.

                            try {
                                JSONObject jo = new JSONObject(response);
                                String status = jo.getString("task");

                                if (status.equalsIgnoreCase("success")) {
                                    Toast.makeText(getApplicationContext(), "feedback Send ", Toast.LENGTH_SHORT).show();

                                    Intent i = new Intent(getApplicationContext(), Customer_home.class);
                                    startActivity(i);
//
//


                                } else {
                                    Toast.makeText(add_feedback.this, "Not Send", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {

                                Toast.makeText(add_feedback.this, "" + e, Toast.LENGTH_SHORT).show();

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(add_feedback.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("Customer_id", sh.getString("lid", ""));
                            params.put("fd", fd);


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
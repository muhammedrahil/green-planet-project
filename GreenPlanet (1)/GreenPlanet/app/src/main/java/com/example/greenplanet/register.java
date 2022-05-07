package com.example.greenplanet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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

public class register extends AppCompatActivity {
EditText firstname,lastname,phone,place,post,pin,email,uname,pwd;
RadioButton male,female;
Button register;
String fname,lname,gen,ph,place1,post1,pin1,email1,username,password;
    String url="";
    String ip="";
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstname=findViewById(R.id.fname);
        lastname=findViewById(R.id.lname);
        male=findViewById(R.id.radioButton);
        female=findViewById(R.id.radioButton2);
        phone=findViewById(R.id.Phone);
        place=findViewById(R.id.place);
        post=findViewById(R.id.post);
        pin=findViewById(R.id.pin);
        email=findViewById(R.id.email);
        uname=findViewById(R.id.uname);
        pwd=findViewById(R.id.pwd);
        register=findViewById(R.id.button6);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ip=sh.getString("ip","");

        url="http://"+ip+":5000/register";

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fname=firstname.getText().toString();
                lname=lastname.getText().toString();
                if(male.isChecked())
                {
                    gen=male.getText().toString();
                }
                else
                {
                    gen=female.getText().toString();
                }
                ph=phone.getText().toString();
                place1=place.getText().toString();
                post1=post.getText().toString();
                pin1=pin.getText().toString();
                email1=email.getText().toString();
                username=uname.getText().toString();
                password=pwd.getText().toString();


                if (fname.equalsIgnoreCase("")) {
                    firstname.setError("Enter Your First Name");
                    firstname.requestFocus();
                } else if (!fname.matches("^[a-zA-Z ]*$")) {
                    firstname.setError("Only characters allowed");
                    firstname.requestFocus();
                }
                else if (lname.equalsIgnoreCase("")) {
                    lastname.setError("Enter Your Second Name");
                    lastname.requestFocus();
                } else if (!lname.matches("^[a-zA-Z ]*$")) {
                    lastname.setError("Only characters allowed");
                    lastname.requestFocus();
                } else if (ph.equalsIgnoreCase("")) {
                    phone.setError("Enter Your Phone No");
                    phone.requestFocus();
                } else if (ph.length()!=10) {
                    phone.setError("Minimum 10 nos required");
                    phone.requestFocus();
                } else if (place1.equalsIgnoreCase("")) {
                    place.setError("Enter Your Place");
                    place.requestFocus();
                } else if (post1.equalsIgnoreCase("")) {
                    post.setError("Enter Your Post");
                    post.requestFocus();
                } else if (pin1.equalsIgnoreCase("")) {
                    pin.setError("Enter Your Pin No");
                    pin.requestFocus();
                } else if (pin1.length()!=6) {
                    pin.setError("Minimum 6 nos required");
                    pin.requestFocus();

                } else if (email1.equalsIgnoreCase("")) {
                    email.setError("Enter Your Email");
                    email.requestFocus();
                } else if (username.equalsIgnoreCase("")) {
                    uname.setError("Enter Your username");
                    uname.requestFocus();
                }   else if(password.equalsIgnoreCase("")) {
                    pwd.setError("Enter Your Password");
                    pwd.requestFocus();
                }
                    else{


                        // Instantiate the RequestQueue.
                        RequestQueue queue = Volley.newRequestQueue(register.this);

                        // Request a string response from the provided URL.
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the response string.

                                try {
                                    JSONObject jo = new JSONObject(response);
                                    String status = jo.getString("task");

                                    if (status.equalsIgnoreCase("success")) {
                                        Toast.makeText(getApplicationContext(), "Registered ", Toast.LENGTH_SHORT).show();
//
                                        startActivity(new Intent(getApplicationContext(), login.class));


                                    } else {
                                        Toast.makeText(register.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {

                                    Toast.makeText(register.this, "" + e, Toast.LENGTH_SHORT).show();

                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(register.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("username", username);
                                params.put("password", password);
                                params.put("fname", fname);
                                params.put("lname", lname);
                                params.put("Gender", gen);
                                params.put("Phone", ph);
                                params.put("Place", place1);
                                params.put("Post", post1);
                                params.put("Pin", pin1);
                                params.put("Email", email1);


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
package com.example.greenplanet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class Order_Product extends AppCompatActivity {

    EditText product,amount;
    Button register,b1,b2;
    String prd,amt,Product_id;

    String url="",img;
    String ip="";
    SharedPreferences sh;
    ImageView im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_product);

        product=findViewById(R.id.product);
        amount=findViewById(R.id.amount1);
        register=findViewById(R.id.button2);
        im=findViewById(R.id.imageView3);
        b1=findViewById(R.id.button3);
        b2=findViewById(R.id.button4);


        product.setText(getIntent().getStringExtra("Product_name"));
        amount.setText(getIntent().getStringExtra("Price"));
        Product_id=getIntent().getStringExtra("Product_id");
        img=getIntent().getStringExtra("image");


        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ip=sh.getString("ip","");



        if(android.os.Build.VERSION.SDK_INT>9)
        {
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

//        i2.setImageDrawable(Drawable.createFromPath(getIntent().getStringExtra("photo"))));
        java.net.URL thumb_u;
        try {

            //thumb_u = new java.net.URL("http://192.168.43.57:5000/static/photo/flyer.jpg");

            thumb_u = new java.net.URL("http://"+sh.getString("ip","")+":5000/static/files/"+img);
            Drawable thumb_d = Drawable.createFromStream(thumb_u.openStream(), "src");
            im.setImageDrawable(thumb_d);

        }
        catch (Exception e)
        {
            Log.d("errsssssssssssss",""+e);
        }



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prd=product.getText().toString();
                amt=amount.getText().toString();

                if(prd.equalsIgnoreCase(""))
                {
                    product.setError("Enter your Product");
                }
                else
                if(amt.equalsIgnoreCase(""))
                {
                    amount.setError("Enter your Amount");
                }
                else {
                    url="http://"+ip+":5000/order_product";


                    // Instantiate the RequestQueue.
                    RequestQueue queue = Volley.newRequestQueue(Order_Product.this);

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the response string.

                            try {
                                JSONObject jo = new JSONObject(response);
                                String status = jo.getString("task");

                                if (status.equalsIgnoreCase("success")) {
                                    Toast.makeText(getApplicationContext(), "Ordered Successfully ", Toast.LENGTH_SHORT).show();
//
//



                                } else {
                                    Toast.makeText(Order_Product.this, "Not Ordered", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {

                                Toast.makeText(Order_Product.this, "" + e, Toast.LENGTH_SHORT).show();

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Order_Product.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("Customer_id", sh.getString("lid", ""));
                            params.put("Product_id", Product_id);
                            params.put("Amount", amt);
                            params.put("billid", sh.getString("billid",""));


                            return params;
                        }
                    };
                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);
                }



            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),View_product.class));

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                url="http://"+ip+":5000/finish";


                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(Order_Product.this);

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.

                        try {
                            JSONObject jo = new JSONObject(response);
                            String status = jo.getString("task");

                            if (status.equalsIgnoreCase("success")) {
                                Toast.makeText(getApplicationContext(), "Finishing ", Toast.LENGTH_SHORT).show();
//
//
                                Intent i = new Intent(getApplicationContext(), Customer_home.class);
                                startActivity(i);


                            } else {
                                Toast.makeText(Order_Product.this, "Not Ordered", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {

                            Toast.makeText(Order_Product.this, "" + e, Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Order_Product.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("Customer_id", sh.getString("lid", ""));
                        params.put("billid", sh.getString("billid",""));


                        return params;
                    }
                };
                // Add the request to the RequestQueue.
                queue.add(stringRequest);



            }
        });
    }
}
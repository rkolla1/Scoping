package com.example.survey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class login extends AppCompatActivity {
    TextView Username;
    TextView password;
    Button login;
    TextView forgot_password;

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        Username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.update);
        forgot_password = findViewById(R.id.forgot);
        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit = mPref.edit();
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RequestQueue queue = Volley.newRequestQueue(login.this);
                String url = "https://z1cbskq9w2.execute-api.us-east-2.amazonaws.com/default/restapi/?email="+Username.getText().toString()+"&password=" + password.getText().toString();
                System.out.println(url);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener <String>() {
                    @Override
                    public void onResponse(String response) {
                        String j_data = hashgen(response);
                        String json_data = j_data.substring(1, j_data.length() - 1);
                        try {
                            JSONObject jo = new JSONObject(json_data);
                            System.out.println(jo.toString());
                            if (jo.has("login") && jo.getString("login").equals("true")) {
                                mEdit.putString("token", jo.getString("token"));
                                mEdit.commit();
                                System.out.println(jo.getString("token"));
                                Intent intent = new Intent(login.this, Teams.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(login.this, "Check your Credentials", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("failure");
                    }
                });
                queue.add(stringRequest);
                /*Intent intent = new Intent(login.this, Teams.class);
                startActivity(intent);*/
            }
        });

    }

        public String hashgen(String str){
            return str.replaceAll("\\\\","");
        }

}

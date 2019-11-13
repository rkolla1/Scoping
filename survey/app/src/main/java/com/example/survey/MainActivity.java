package com.example.survey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {

    Button login;
    EditText username,password;
    TextView forgotpassword,signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        login = findViewById(R.id.update);
        username = findViewById(R.id.username);
        forgotpassword = findViewById(R.id.forgot);
        signin= findViewById(R.id.signin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                request_parsing();
                //Intent intent = new Intent(MainActivity.this,Question.class);
                //startActivity(intent);

            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,login.class);
                startActivity(intent);
            }
        });

    }
    public void request_parsing(){
        System.out.println("inside request parsing");
        RequestQueue queue= Volley.newRequestQueue(MainActivity.this);
        String url="https://z1cbskq9w2.execute-api.us-east-2.amazonaws.com/default/emailLogin/?email="+username.getText().toString();
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String json=hashgen(response);
                String j_data=json.substring(1,json.length()-1);
                try {
                    JSONObject jo = new JSONObject(j_data);
                    if(jo.has("login") && jo.getString("login").equals("false")){
                        Toast.makeText(getApplicationContext(),"Sorry you are not a certified grader",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"congrats you are registered,please view your email to login",Toast.LENGTH_SHORT).show();
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
    }
    public String hashgen(String str){
        return str.replaceAll("\\\\","");
    }
}

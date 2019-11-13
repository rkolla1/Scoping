package com.example.survey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

public class Update_details extends AppCompatActivity {
    EditText password;
    Button update;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);
        getSupportActionBar().hide();

        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit = mPref.edit();
        Log.d("status","Inside update_details");
        password=findViewById(R.id.password);
        update=findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getText().toString().equals("")){
                    password.setError("Please enter the password");
                }
                else{
                setPassword();}

            }
        });

    }
    public void setPassword(){
        String token=mPref.getString("token","");
        RequestQueue queue= Volley.newRequestQueue(this);
        String url="https://z1cbskq9w2.execute-api.us-east-2.amazonaws.com/default/update/?token="+token+"&password="+password.getText().toString();
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String j_data=hashgen(response);
                String json_data=j_data.substring(1,j_data.length()-1);
                try {
                    JSONObject jo=new JSONObject(json_data);
                    if(jo.has("message")&&jo.getString("message").equals("successful")){
                        finish();
                    }
                    else{
                        Toast.makeText(Update_details.this,"Update not successful,Please check your internet connection",Toast.LENGTH_SHORT).show();
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

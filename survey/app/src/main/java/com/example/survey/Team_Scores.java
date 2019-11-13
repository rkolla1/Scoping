package com.example.survey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.JsonReader;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Team_Scores extends AppCompatActivity {

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEdit;

    ListView listView;

    String token;

    String[] arraynames = {"Team a","Team b","Team c","Team d","Team e"};
    LinkedHashMap<String,Double> scoredetails = new LinkedHashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team__scores);

        getSupportActionBar().hide();

        Log.d("status","inside team_scores");
        mPref=PreferenceManager.getDefaultSharedPreferences(this);
        mEdit = mPref.edit();
        token=mPref.getString("token","");

        scoredetails.put(arraynames[0],0.0);
        scoredetails.put(arraynames[1],0.0);
        scoredetails.put(arraynames[2],0.0);
        scoredetails.put(arraynames[3],0.0);
        scoredetails.put(arraynames[4],0.0);


        RequestQueue queue= Volley.newRequestQueue(this);
        String url="https://z1cbskq9w2.execute-api.us-east-2.amazonaws.com/default/board?token="+token;
        Log.d("url",url);
        StringRequest stringRequest=new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response",response);
                String j_data=response.replaceAll("\\\\","");
                Log.d("json",j_data);
                String json_data=j_data.substring(1,j_data.length()-1);
                Log.d("json",json_data);
                try {
                    JSONObject jo=new JSONObject(json_data);
                    JSONArray j_array=jo.getJSONArray("result");
                    for(int i=0;i<j_array.length();i++){
                        Log.d("json_data", String.valueOf((j_array.get(i))));
                        String key = arraynames[i];
                        scoredetails.put(key,(Double) j_array.get(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                System.out.println(response);
                System.out.println(scoredetails);

                List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(scoredetails.entrySet());

                // Sorting the list based on values
                Collections.sort(list, new Comparator<Map.Entry<String, Double>>()
                {
                    public int compare(Map.Entry<String, Double> o1,
                                       Map.Entry<String, Double> o2) {
                        return o2.getValue().compareTo(o1.getValue());
                    }

                });

                System.out.println(list.get(0).getValue());

                listView = findViewById(R.id.listview2);
                Teams_Scores_Adapter customAdapter = new Teams_Scores_Adapter(Team_Scores.this, list);
                listView.setAdapter(customAdapter);



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("failure");
            }
        });
        queue.add(stringRequest);


    }


}

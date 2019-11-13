package com.example.survey;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.view.LayoutInflater;
import android.view.View;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Teams_Adapter extends BaseAdapter {
    Context context;
    String TeamList[];
    LayoutInflater inflter;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEdit;


    public Teams_Adapter(Context applicationContext, String[] TeamList) {
        this.context = applicationContext;
        this.TeamList = TeamList;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return TeamList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.team_item, null);
        TextView teamn = (TextView) view.findViewById(R.id.team_name);
        teamn.setText(TeamList[i]);

        ImageView mImgCheck = (ImageView) view.findViewById(R.id.imageView);
        grade_checking(teamn.getText().toString(),mImgCheck);
        ((Animatable) mImgCheck.getDrawable()).start();

        return view;

    }

    public void grade_checking(String team_name, final ImageView img) {
        System.out.println(context);
        System.out.println("team_name "+team_name);
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
        String token=mPref.getString("token","");
        RequestQueue queue = Volley.newRequestQueue(context);
        //boolean return_value=true;
        String url = "https://z1cbskq9w2.execute-api.us-east-2.amazonaws.com/default/check/?token="+token+"&team="+team_name;
        System.out.println("URL : "+url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String j_data=hashgen(response);
                System.out.println("Response : "+j_data);
                String json_data=j_data.substring(1,j_data.length()-1);
                try {
                    JSONObject jo = new JSONObject(json_data);
                    if(!(jo.has("check")&&jo.getString("check").equals("true"))){
                        img.setVisibility(View.INVISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Failure");
            }
        });
        queue.add(stringRequest);
    }
    public String hashgen(String str){
        return str.replaceAll("\\\\","");
    }
}




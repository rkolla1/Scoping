package com.example.survey;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Teams_Scores_Adapter extends BaseAdapter {
    Context context;
    List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>();
    LayoutInflater inflter;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEdit;

    public Teams_Scores_Adapter(Context applicationContext,List<Map.Entry<String, Double>> list) {
        this.context = applicationContext;
        this.list = list;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return list.size();
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
        view = inflter.inflate(R.layout.team_item_score, null);
        TextView teamn = (TextView) view.findViewById(R.id.team_name1);
        TextView score = (TextView) view.findViewById(R.id.score);
        teamn.setText(list.get(i).getKey());
        //score.setText(list.get(i).getValue().toString());
        score.setText(String.format("%.2f", list.get(i).getValue()));




        return view;
    }
}

package com.example.survey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Teams extends AppCompatActivity {

    ListView listView;
    String Teamlist[] = {"Team a", "Team b", "Team c", "Team d", "Team e"};
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEdit;
    String token;
    public static String TEAM_NAME="team_name";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);


        this.setTitle("Teams");

        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit = mPref.edit();
        token= mPref.getString("token","");
        listView = findViewById(R.id.listview);
        Teams_Adapter customAdapter = new Teams_Adapter(this, Teamlist);
        listView.setAdapter(customAdapter);
        Intent intent=getIntent();
        if(intent.getData()!=null) {
            Log.d("status","inside if condition");
            Uri Data = intent.getData();

            Log.d("uri",Data.toString());
            token = Data.toString().substring(16);
            Log.d("status",token);
            mEdit.putString("token", token);
            mEdit.commit();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i1=new Intent(Teams.this,Question.class);
                i1.putExtra(TEAM_NAME,Teamlist[position]);
                startActivity(i1);
            }
        });


    }

    @Override
    public void onBackPressed() { }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.update:
                Log.d("status","Update_home");
                Intent intent=new Intent(Teams.this,Update_details.class);
                startActivity(intent);
                break;

            case R.id.board:
                Log.d("status","Update_home");
                Intent in = new Intent(Teams.this,Team_Scores.class);
                startActivity(in);
        }

        return true;
    }
}

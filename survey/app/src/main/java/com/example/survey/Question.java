package com.example.survey;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Question extends AppCompatActivity {

    Button next;
    TextView question_text;
    TextView question_name;
    Button submit;
    LinkedHashMap<String,String> q_details=new LinkedHashMap<String,String>();
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEdit;
    RadioGroup radioGroup;
    int sum=0;
    String team_name;

    //List<String> questions = new ArrayList<String>(Arrays.asList(question1, question2, question3, question4, question5, question6, question7));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        question_text = findViewById(R.id.question);
        question_name=findViewById(R.id.question_no);
        submit=findViewById(R.id.submit);
        radioGroup=findViewById(R.id.RadioGroup);
        radioGroup.clearCheck();
        final Intent intent=getIntent();
        team_name=intent.getStringExtra(Teams.TEAM_NAME);
        this.setTitle(team_name);
        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit = mPref.edit();
        get_questions();
        final Iterator iterator = q_details.entrySet().iterator();
        question_updation(iterator);
        next = findViewById(R.id.next);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sum_calculation()) {
                    radioGroup.clearCheck();
                    question_updation(iterator);
                }

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sum_calculation();
                score_submission();
                Toast.makeText(getApplicationContext(),String.valueOf(sum),Toast.LENGTH_SHORT).show();
                Intent intent1=new Intent(Question.this,Teams.class);
                startActivity(intent1);
            }
        });
    }
    public void get_questions(){
        String question1 = "Poster content is of professional quality and indicates a mastery of the project subject matter";
        String question2 = "The Presentation is organized,engaging and includes a through description of the design and implementation of" +
                "the design";
        String question3 = "All the team members are suitably attired,are polite,demonstrate full knowledge of material, and can answer " +
                "all relevant questions";
        String question4 = "The work product(model,prototype,documentation set or computer simulation) is of professional quality in all respects.";
        String question5 = "The team implemented novel approaches and/or solutions in the development of project";
        String question6 = "The project has the potential to enhance the reputation of the innovative Computing project and/or CCI/DSI";
        String question7 = "The team successfully explained the scope and results of their project in no moe than 5 minutes.";
        q_details.put("question1",question1);
        q_details.put("question2",question2);
        q_details.put("question3",question3);
        q_details.put("question4",question4);
        q_details.put("question5",question5);
        q_details.put("question6",question6);
        q_details.put("question7",question7);

    }
    public void question_updation(Iterator iterator){
        //final Iterator iterator = q_details.entrySet().iterator();
        //Map.Entry<String,String> map_element=(Map.Entry<String,String>)iterator.next();
        if(iterator.hasNext()) {
            Map.Entry<String, String> map_element = (Map.Entry<String, String>) iterator.next();
            if(map_element.getKey().equals("question7")){
                submit.setVisibility(View.VISIBLE);
                next.setVisibility(View.INVISIBLE);
            }
            question_name.setText(map_element.getKey());
            question_text.setText(map_element.getValue());
        }

    }
    public boolean sum_calculation(){
        int selected_id=radioGroup.getCheckedRadioButtonId();
        if(selected_id==-1){
            Toast.makeText(getApplicationContext(),"Choose an id",Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            RadioButton radioButton = findViewById(selected_id);
            sum += Integer.parseInt(radioButton.getText().toString());
        }
        return true;


    }
    public void score_submission(){
        Log.d("status","Inside score submission");
        String token=mPref.getString("token","");
        RequestQueue queue= Volley.newRequestQueue(this);
        String url="https://z1cbskq9w2.execute-api.us-east-2.amazonaws.com/default/score/?score="+String.valueOf(sum)+"&token="+token+"&team="+team_name;
        StringRequest stringRequest=new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("failure");
            }
        });
            queue.add(stringRequest);
        }

    @Override
    public void onBackPressed() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit without completing the survey")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();

    }
}


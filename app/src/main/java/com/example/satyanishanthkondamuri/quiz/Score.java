package com.example.satyanishanthkondamuri.quiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.satyanishanthkondamuri.quiz.Common.Common;

public class Score extends AppCompatActivity {
    int Score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        TextView ques = findViewById(R.id.total_q);
        TextView result = findViewById(R.id.score);

        for (int i=0 ;i<Common.answers.size() ;i++)
        {
            if(!(Common.answers.get(i).getAnswer() ==""))
            {
                    if(Common.questions.get(i).getAnswer().equals(Common.answers.get(i).getAnswer()));
                {
                           Score++;
                }
            }
        }

                   result.setText(Score+"/"+String.valueOf(Common.questions.size()));
                    ques.setText(String.valueOf(Common.questions.size()));
                   Common.answers.clear();

    }

}

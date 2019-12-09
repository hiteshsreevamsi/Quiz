package com.example.satyanishanthkondamuri.quiz;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.satyanishanthkondamuri.quiz.Common.Common;

public class Recycler_question extends RecyclerView.Adapter<questionno> {
    int ques_no =1;
    Context c;
    RecyclerView rv;
    public  Recycler_question(Context c,RecyclerView rv)
    {
        this.c = c;
        this.rv = rv;
    }
    @NonNull
    @Override
    public questionno onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_number,parent,false);
        final TextView number = v.findViewById(R.id.no);

       v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Game_play.Update_Answer(Game_play.mcqs.getCheckedRadioButtonId(),Game_play.mcqs.getRootView());
                Game_play.mcqs.clearCheck();
                Game_play.Question_cursor= Integer.parseInt(number.getText().toString())-1;
                Game_play.hold.setChecked(false);
                Game_play.Question_set(Game_play.Question_cursor);
                Game_play.Check_answered(Game_play.mcqs.getRootView(), Game_play.Question_cursor);
                FragmentManager fm = ((Activity)c).getFragmentManager();
                DialogFragment df = (DialogFragment) fm.findFragmentByTag("question_dialog");
                df.dismiss();
            }
        });
        questionno question = new questionno(v);
        return question;
    }

    private void Colour_change(int i,questionno holder) {
        if(Common.answers.get(i).getHold().booleanValue()==true)
        {
            CardView cardView = holder.itemView.findViewById(R.id.ques_recycle);
            cardView.setCardBackgroundColor(Color.parseColor("#FFFF00"));
        }
        else if(!Common.answers.get(i).getAnswer().equals("")){
            CardView cardView = holder.itemView.findViewById(R.id.ques_recycle);
            cardView.setCardBackgroundColor(Color.parseColor("#008000"));
        }
        else {
            CardView cardView = holder.itemView.findViewById(R.id.ques_recycle);
            cardView.setCardBackgroundColor(Color.parseColor("#D3D3D3"));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull questionno holder, int position) {
        holder.ques_no.setText(String.valueOf(position+1));
        Colour_change(position,holder);


    }

    @Override
    public int getItemCount() {
        return Common.questions.size();
    }
}

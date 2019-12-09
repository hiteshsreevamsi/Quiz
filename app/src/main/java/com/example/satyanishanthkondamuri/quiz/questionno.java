package com.example.satyanishanthkondamuri.quiz;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class questionno extends RecyclerView.ViewHolder {
    public TextView ques_no;
    public questionno(View itemView) {
        super(itemView);
        ques_no = itemView.findViewById(R.id.no);
    }
}

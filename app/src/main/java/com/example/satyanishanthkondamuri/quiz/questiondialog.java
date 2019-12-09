package com.example.satyanishanthkondamuri.quiz;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.satyanishanthkondamuri.quiz.Common.Common;

public class questiondialog extends DialogFragment implements View.OnClickListener {

    RecyclerView rv;
    static Recycler_question adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.questiondialog,null);
        rv = v.findViewById(R.id.qudialog);
        Button dismiss = v.findViewById(R.id.dismiss);
        setCancelable(false);
        rv.setLayoutManager(new GridLayoutManager(this.getActivity(), 5));
        adapter= new Recycler_question(this.getActivity(),rv);
        rv.setAdapter(adapter);
        this.getDialog().setTitle("Question numbers");
        dismiss.setOnClickListener(this);


        return v;
    }

    @Override
    public void onClick(View view) {
        dismiss();

    }
}

package com.example.satyanishanthkondamuri.quiz;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.satyanishanthkondamuri.quiz.Time.Time;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Start_up extends AppCompatActivity {
    FirebaseDatabase Database;
    DatabaseReference mDatabaseReference;
    String Start,End;
    Calendar current,plusfive;
    Date df1,df2;
    Long diff;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        final Button b = findViewById(R.id.attempt);
        b.setVisibility(View.INVISIBLE);
        final TextView mTextField = findViewById(R.id.ttime);
        final TextView txt  = findViewById(R.id.start);
        progressBar=findViewById(R.id.progressBarCircle);
        Database = FirebaseDatabase.getInstance();
        mDatabaseReference = Database.getReference("Time_details");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Start= dataSnapshot.child("Time").getValue(Time.class).gettime_start();
                End= dataSnapshot.child("Time").getValue(Time.class).gettime_end();
                Log.e("db_time",Start);
                Calendar start_time=Calendar.getInstance();
                current = Calendar.getInstance();
                plusfive=Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                SimpleDateFormat sd = new SimpleDateFormat("HH:mm");


                Calendar end_time=Calendar.getInstance();
                SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                try {
                    start_time.setTime(sdf.parse(Start));
                    plusfive.setTime(sd.parse(Start));
                    plusfive.add(Calendar.MINUTE,5);
                    end_time.setTime(sdf1.parse(End));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //Log.e("time after",start_time.toString());
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
                SimpleDateFormat sdf3=new SimpleDateFormat("HH:mm");

                String Current_time = sdf3.format(current.getTime());

                String limit = sdf2.format(plusfive.getTime());

                Log.e("time current",Current_time);
                Log.e("time limit",limit);
                Log.e("real time",current.getTime().toString());




                if(Current_time.compareTo(Start)>=0 && limit.compareTo(Current_time)>=0)
                {

                    Log.e("time after","hello");
                    b.setVisibility(View.VISIBLE);
                    txt.setVisibility(View.INVISIBLE);


                }
                try {
                    df1 = sdf2.parse(Start);

                    df2=sdf2.parse(sdf2.format(current.getTime()));
                    diff=(df2.getTime()-df1.getTime());
                    if(diff<0)
                    {
                        diff= df1.getTime()-df2.getTime();

                    }
                    else if(diff>300000)
                    {
                        mTextField.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        txt.setText("The Quiz has expired");


                    }
                    progressBar.setMax((int)(diff/1000));

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new CountDownTimer((diff), 1000) {

                    public void onTick(long millis) {
                        String text = String.format("%02d:%02d:%02d",
                                TimeUnit.MILLISECONDS.toHours(millis),
                                TimeUnit.MILLISECONDS.toMinutes(millis) -
                                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
                                TimeUnit.MILLISECONDS.toSeconds(millis) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                        progressBar.setProgress((int)millis/1000);
                        mTextField.setText(text);
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {
                        mTextField.setText("All The Best");
                        b.setVisibility(View.VISIBLE);
                        txt.setVisibility(View.INVISIBLE);
                    }

                }.start();

                Log.e("time diff",diff.toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.e("database error",databaseError.toString());

            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Start_up.this,Game_play.class);
                startActivity(i);
                finish();

            }
        });






    }

    @Override
    protected void onStart() {
        super.onStart();








    }
}



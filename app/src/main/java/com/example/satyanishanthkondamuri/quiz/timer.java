package com.example.satyanishanthkondamuri.quiz;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class timer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        final ProgressBar progressBar;
        final TextView time;
        long timemilliseconds=60000;

        progressBar=findViewById(R.id.progressBarCircle);
        progressBar.setMax((int)timemilliseconds/1000);
        time=findViewById(R.id.ttime);


        new CountDownTimer(timemilliseconds, 1000) {
            @Override
            public void onTick(long millis) {
                String text = String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millis),
                        TimeUnit.MILLISECONDS.toMinutes(millis) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
                        TimeUnit.MILLISECONDS.toSeconds(millis) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                         time.setText(text);
                         progressBar.setProgress((int)millis/1000);

            }

            @Override
            public void onFinish() {

            }
        };
    }


}

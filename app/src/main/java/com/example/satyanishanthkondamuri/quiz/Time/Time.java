package com.example.satyanishanthkondamuri.quiz.Time;

public class Time {
    public String time_start;
    public String time_end;

    Time(){

    }
    Time(String time_start,String time_end){
        this.time_start=time_start;
        this.time_end = time_end;
    }

    public String gettime_start() {
        return time_start;
    }

    public void settime_start(String time_start) {
        this.time_start = time_start;
    }

    public String gettime_end() {
        return time_end;
    }

    public void settime_end(String Time_end) {
        this.time_end = Time_end;
    }
}

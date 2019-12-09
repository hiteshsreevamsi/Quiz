package com.example.satyanishanthkondamuri.quiz.Answers;

public class Answers {
    String answer;
    Boolean hold;
    int ans_bt_id;
    Answers(){}

    public Answers(String answer,  int ans_bt_id,Boolean hold) {
        this.answer = answer;

        this.ans_bt_id = ans_bt_id;
        this.hold =hold;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean getHold() {
        return hold;
    }

    public void setHold(Boolean hold) {
        this.hold = hold;
    }

    public int getAns_bt_id() {
        return ans_bt_id;
    }

    public void setAns_bt_id(int ans_bt_id) {
        this.ans_bt_id = ans_bt_id;
    }
}

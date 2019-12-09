package com.example.satyanishanthkondamuri.quiz;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.satyanishanthkondamuri.quiz.Answers.Answers;
import com.example.satyanishanthkondamuri.quiz.Common.Common;
import com.example.satyanishanthkondamuri.quiz.Questions.Questions_quiz;
import com.example.satyanishanthkondamuri.quiz.User.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Game_play extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fm;
    questiondialog quesno;
   static Switch  hold;
   static int adapter_change=0;
    NavigationView navigationView;
    TextView Countdown;


    static RadioGroup mcqs;
    static RadioButton btna,btnb,btnc,btnd;
    Button pass,next,previous,sumbitq;
    static TextView Question,Questionno;
    String Category_id="1";
    Menu nav_menu;
    TextView usernametxt,email_idtxt;

   static int Question_cursor=0;

    FirebaseDatabase Database;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        if(savedInstanceState!=null)
        {
            Log.e("saved","success");
           Question_cursor =savedInstanceState.getInt("Question_Cursor");
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Database = FirebaseDatabase.getInstance();
        mDatabaseReference = Database.getReference("Question");
        Countdown = findViewById(R.id.count_down);
        Question = findViewById(R.id.Question);
        Questionno=findViewById(R.id.Questionno);
        pass = findViewById(R.id.pass);
        previous =findViewById(R.id.previous);
        sumbitq = findViewById(R.id.sumbit);
        next = findViewById(R.id.next);
        mcqs = findViewById(R.id.radioGroup);
        btna = findViewById(R.id.radioButton1);
        btnb = findViewById(R.id.radioButton2);
        btnc = findViewById(R.id.radioButton3);

        btnd = findViewById(R.id.radioButton4);
        hold = findViewById(R.id.hold);

        Question_Loader(Question_cursor);

        fm = getFragmentManager();
//    hold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//        @Override
//        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//            if(b)
//            {
//
//            }
//        }
//    });
//
//        mcqs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//
//
//
//            }
//        });



        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Question_cursor==0)
                {
                    Update_Answer(mcqs.getCheckedRadioButtonId(), mcqs.getRootView());
                    mcqs.clearCheck();

                }
                else
                {
                    Update_Answer(mcqs.getCheckedRadioButtonId(),mcqs.getRootView());

                    Question_cursor=Question_cursor -1;
                    mcqs.clearCheck();
                    hold.setChecked(false);

                    Log.e("cursor prev", String.valueOf(Question_cursor));
                }
                Question_set(Question_cursor);
                Check_answered(mcqs.getRootView(),Question_cursor);




            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("On click q size", String.valueOf(Common.questions.size()));
                if((Question_cursor)==Common.questions.size()-1)
                {
                    Update_Answer(mcqs.getCheckedRadioButtonId(), mcqs.getRootView());
                    mcqs.clearCheck();
                }
                else
                {
                    Update_Answer(mcqs.getCheckedRadioButtonId(), mcqs.getRootView());

                    mcqs.clearCheck();
                    Question_cursor=Question_cursor + 1;
                    hold.setChecked(false);
                    Log.e("cursor next", String.valueOf(Question_cursor));
                }
                Question_set(Question_cursor);
                Check_answered(mcqs.getRootView(), Question_cursor);




            }
        });

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mcqs.clearCheck();


            }
        });

        sumbitq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Update_Answer(mcqs.getCheckedRadioButtonId(), mcqs.getRootView());
                Verify_allanswered();
            }
        });


    CountDownTimer timer = new CountDownTimer(300 *1000, 1000) {

        public void onTick(long millisUntilFinished) {
            String text = String.format(Locale.getDefault(), " %02d min: %02d sec",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
            Countdown.setText(text);
            if(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60<10)
            {
                Countdown.setTextColor(Color.parseColor("#FF0000"));
            }
            //here you can have your logic to set text to edittext
        }

        public void onFinish() {
            Update_Answer(mcqs.getCheckedRadioButtonId(), mcqs.getRootView());
            Intent Score = new Intent(Game_play.this,Score.class);
            Game_play.this.startActivity(Score);
            Question_cursor=0;

            finish();
            Countdown.setText("done!");
        }

    }.start();

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                Update_Answer(mcqs.getCheckedRadioButtonId(), mcqs.getRootView());

                if(!(slideOffset==0))
                {


                }


            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {



                nav_menu.add("Questions on hold ");
                for (int i = 0; i < Common.answers.size(); i++) {

                    if(Common.answers.get(i).getHold()){

                        nav_menu.add("On hold "+ (i+1));
                    }

                }


            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                    nav_menu.clear();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

                if(newState == drawer.STATE_DRAGGING)
                {

                }


            }
        });



       navigationView= (NavigationView) findViewById(R.id.nav_view);
         nav_menu = navigationView.getMenu();


      navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        /*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
       usernametxt = (TextView)header.findViewById(R.id.username);
        email_idtxt = (TextView)header.findViewById(R.id.email_id);
        usernametxt.setText(Common.currentuser.getUser_name());
        email_idtxt.setText(Common.currentuser.getEmail());


    }

    private void Verify_allanswered() {
        int unanswerd_questions=0;
        for(int i=0;i<Common.answers.size();i++)
        {
            if(Common.answers.get(i).getAnswer()=="")
            {
                unanswerd_questions++;
            }

        }

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Are you sure you want to submit")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        if(!(unanswerd_questions ==0))
        {
            ((AlertDialog) dialog).setMessage("You have "+unanswerd_questions+" questions unanswered");
        }



        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                Button b = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        Intent Score = new Intent(Game_play.this,Score.class);
                        Game_play.this.startActivity(Score);
                        Question_cursor=0;

                        finish();



                    }
                });
                Button c = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                c.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        dialog.dismiss();

                    }
                });


            }
        });
        dialog.show();
    }

    public static void Check_answered(View v, int question_cursor) {



                if (!Common.answers.get(Question_cursor).getAnswer().equals("")) {
                    RadioButton radioButton = v.findViewById(Common.answers.get(Question_cursor).getAns_bt_id());
                    radioButton.setChecked(true);


                }
       if (Common.answers.get(question_cursor).getHold().booleanValue()==true) {
           hold.setChecked(true);
       }




    }

    public static void Update_flag() {

            Common.answers.get(Question_cursor).setHold(true);

            }

    public static void Question_set(int question_cursor) {
        Questionno.setText(("Q"+(Question_cursor+1)+"."));
         Question.setText(Common.questions.get(question_cursor).getQuestion());
        btna.setText(Common.questions.get(question_cursor).getOption_a());
        btnb.setText(Common.questions.get(question_cursor).getOption_b());
        btnc.setText(Common.questions.get(question_cursor).getOption_c());
        btnd.setText(Common.questions.get(question_cursor).getOption_d());


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit? ");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();



        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game_play, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if(adapter_change!=0)
        questiondialog.adapter.notifyDataSetChanged();
        Update_Answer(mcqs.getCheckedRadioButtonId(), mcqs.getRootView());
          quesno = new questiondialog();
          quesno.show(fm,"question_dialog");
          adapter_change=1;



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
        public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        String q_no = item.getTitle().toString().replace("On hold ","");
        if(!q_no.contains("Questions")) {
            Game_play.mcqs.clearCheck();
            Game_play.Question_cursor = Integer.parseInt(q_no) - 1;
            Game_play.hold.setChecked(false);
            Game_play.Question_set(Game_play.Question_cursor);
            Game_play.Check_answered(Game_play.mcqs.getRootView(), Game_play.Question_cursor);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void Question_Loader(final int question_cursor)
    {

        final ProgressDialog dialog = ProgressDialog.show(Game_play.this, "",
                "Loading", true);
        dialog.show();

        if(!Common.questions.isEmpty())
            Common.questions.clear();

        mDatabaseReference.orderByChild("category_id").equalTo(Category_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postsnapshot : dataSnapshot.getChildren())
                {


                    Log.e("Entered on data change","question load");
                    Questions_quiz question = postsnapshot.getValue(Questions_quiz.class);
                    Common.questions.add(question);

                    Question_set(question_cursor);
                    dialog.dismiss();
                }
                Log.e("questions size ", String.valueOf(Common.questions.size()));
                for(int i =0 ; i<Common.questions.size();i++)
                {
                    Answers ans = new Answers("", -1,false);
                    Common.answers.add(i, ans);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.e("error",databaseError.toString());

            }
        });




    }

    public static void Update_Answer(int i, View v)
    {


        if(i == -1)
        {

            Log.e("no radio selected","i=-1");
            Answers ans = new Answers("", 0,hold.isChecked());
            Common.answers.set(Question_cursor, ans);


        }
        else {
            RadioButton rb = v.findViewById(i);
            String answer = rb.getText().toString();
                Answers ans = new Answers(answer, i,hold.isChecked());
                Common.answers.set(Question_cursor, ans);


        }

    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        Bundle savedInstanceState = new Bundle();
//        savedInstanceState.putInt("Question_Cursor",Question_cursor);
//        onSaveInstanceState(savedInstanceState);
//    }

//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        super.onSaveInstanceState(savedInstanceState);
//    savedInstanceState.putInt("Question_Cursor",Question_cursor);
//
//    }


}


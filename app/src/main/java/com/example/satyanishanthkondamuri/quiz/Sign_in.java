package com.example.satyanishanthkondamuri.quiz;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.loopj.android.http.*;


import com.example.satyanishanthkondamuri.quiz.Common.Common;
import com.example.satyanishanthkondamuri.quiz.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METLengthChecker;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.Inflater;

import cz.msebera.android.httpclient.entity.mime.Header;

public class Sign_in extends AppCompatActivity {

    FirebaseDatabase database;

    DatabaseReference databaseReference;
    SharedPreferences sharedpreferences;
    private static final String PREFS_NAME = "preferences";
    private static final String PREF_UNAME = "Username";
    private static final String PREF_PASSWORD = "Password";

    private final String DefaultUnameValue = "";
    private String UnameValue;

    private final String DefaultPasswordValue = "";
    private String PasswordValue;

    Context context;
    ConstraintLayout constraintLayout;
    FirebaseAuth mAuth;
    MaterialEditText newuser,newemail,newpass,newpassconfirm; // for sign up
    TextInputLayout passcounter;
    AppCompatButton sign_in;
    AppCompatEditText user,pass;

    AppCompatButton sign_up;

    public Sign_in() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

//        try{
//
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpResponse response = httpclient.execute(new HttpGet("https://google.com/"));
//            StatusLine statusLine = response.getStatusLine();
//            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
//                String dateStr = response.getFirstHeader("Date").getValue();
//                //Here I do something with the Date String
//                System.out.println(dateStr);
//                Log.e("date",dateStr);
//
//            } else{
//                //Closes the connection.
//                response.getEntity().getContent().close();
//                throw new IOException(statusLine.getReasonPhrase());
//            }
//        }catch (ClientProtocolException e) {
//            Log.d("Response", e.getMessage());
//        }catch (IOException e) {
//            Log.d("Response", e.getMessage());
//        }


        //forsigin


       database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Users");
        mAuth = FirebaseAuth.getInstance();

        constraintLayout = findViewById(R.id.sigin_layout);
        constraintLayout.setOnClickListener(null);
        user = findViewById(R.id.user);
        pass = findViewById(R.id.password);

        sign_in = findViewById(R.id.sign_in);
        sign_up = findViewById(R.id.sign_up);
        passcounter = findViewById(R.id.pass_ext);
        passcounter.setCounterEnabled(true);
        passcounter.setCounterMaxLength(8);

         context = this;
        sharedpreferences = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(PREF_UNAME)) {
            user.setText(sharedpreferences.getString(PREF_UNAME, DefaultUnameValue));
        }
        if (sharedpreferences.contains(PREF_PASSWORD)) {
            pass.setText(sharedpreferences.getString(PREF_PASSWORD, DefaultPasswordValue));

        }

        user.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(user.getText().toString().isEmpty())
                {
                    user.setError("Username can't be empty");


                }
            }
        });
        user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(user.getText().toString().isEmpty())
                {
                    user.setError("Username can't be empty");

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(pass.getText().toString().isEmpty())
                {
                    pass.setError("Password can't be empty");
                    passcounter.setPasswordVisibilityToggleEnabled(false);


                }
                else {
                    passcounter.setPasswordVisibilityToggleEnabled(false);

                }
            }
        });
       pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(pass.getText().toString().isEmpty())
                {
                    pass.setError("Password can't be empty");
                    passcounter.setPasswordVisibilityToggleEnabled(false);

                }
                else {
                    passcounter.setPasswordVisibilityToggleEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = ProgressDialog.show(Sign_in.this, "",
                        "Signing Up", true);
                dialog.show();
                show_signup_dialog();
                dialog.dismiss();
            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {






                Signin(user.getText().toString(),pass.getText().toString());




            }
        });

    }



    private void Signin(final String username, final String password){

        final ProgressDialog dilalog = new ProgressDialog(this);
        dilalog.setMessage("Signing in");
        dilalog.show();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(!username.isEmpty())
               {

                   if(dataSnapshot.child(username).exists())
                   {
                       if(password.isEmpty())
                       {

                           Toast.makeText(Sign_in.this,"Password is Empty",Toast.LENGTH_LONG).show();
                           dilalog.dismiss();
                       }
                      else if(dataSnapshot.child(username).getValue(User.class).getPassword().equals(password))
                       {


                           Signinfb(dataSnapshot,username,password);
                           Common.currentuser = new User(dataSnapshot.child(username).getValue(User.class).getUser_name(),dataSnapshot.child(username).getValue(User.class).getPassword(),dataSnapshot.child(username).getValue(User.class).getEmail());
                           dilalog.dismiss();

                       }

                       else
                       {

                           Toast.makeText(Sign_in.this,"Wrong password",Toast.LENGTH_LONG).show();
                           dilalog.dismiss();
                       }

                   }
                   else
                   {

                       Toast.makeText(Sign_in.this,"Incorrect username",Toast.LENGTH_LONG).show();
                       dilalog.dismiss();

                   }
               }

               else {

                   Toast.makeText(Sign_in.this,"Username is empty",Toast.LENGTH_LONG).show();
                   dilalog.dismiss();
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dilalog.dismiss();



            }
        });



    }

    private void Signinfb(final DataSnapshot dataSnapshot, final String username, final String password) {
        mAuth.signInWithEmailAndPassword(dataSnapshot.child(username).getValue(User.class).getEmail(), password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e("Sigin", "signInWithEmail:success");

                            FirebaseUser userfb = mAuth.getCurrentUser();
                            if(!userfb.isEmailVerified())
                            {
                                Log.e("reverify","Entered");
                                userfb.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.e("mail sent", "Email sent.");
                                                    Toast.makeText(Sign_in.this,"Please Verify to continue",Toast.LENGTH_LONG).show();



                                                }
                                                else {
                                                    Log.e("reverify",task.getException().toString());

                                                }
                                            }
                                        });


                            }
                            else {
                                    User present_user = new User(username,password,dataSnapshot.child(username).getValue(User.class).getEmail());
                                Common.currentuser = present_user;
                                Intent_call();
                                finish();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e("why me", "signInWithEmail:failure" + task.getException());
                            Toast.makeText(Sign_in.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();


                        }

                        // ...
                    }

                });
    }

    private void Intent_call() {

        Intent start_up = new Intent(Sign_in.this,Start_up.class);
       Sign_in.this.startActivity(start_up);
       onPause();
       finish();
    }

    private void show_signup_dialog() {

        final Context gd = this;


        LayoutInflater Inflater = this.getLayoutInflater();
        View view = Inflater.inflate(R.layout.sign_up, null);
        newuser = view.findViewById(R.id.newuser);
        newemail = view.findViewById(R.id.email);
        newpass = view.findViewById(R.id.newpassword);
        newpassconfirm = view.findViewById(R.id.newconifrmpassword);

        newpass.setMinCharacters(6);
        newpass.setMaxCharacters(8);
        newuser.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (newuser.getText().toString().isEmpty()) {
                    newuser.setError("Username can't be empty");
                }
            }
        });
        newpass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (newpass.getText().toString().isEmpty()) {
                    newpass.setError("Password can't be empty");
                }
            }
        });
        newemail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (newemail.getText().toString().isEmpty()) {
                    newemail.setError("Email can't be empty");
                }
            }
        });

        final AlertDialog  mdialog = new AlertDialog.Builder(this)
                .setView(view)
                .setMessage("Fill up all the credentials")
                .setTitle("Sign Up")
                .setIcon(R.drawable.ic_person_black_24dp)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        mdialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {



                Button b = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        final ProgressDialog dilalog = new ProgressDialog(gd);
                        dilalog.setMessage("Loading");
                        // TODO Do something


                        dilalog.show();

                        if ((newuser.getText().toString().isEmpty()) || (newemail.getText().toString().isEmpty()) || (newpass.getText().toString().isEmpty())) {
                            Toast.makeText(Sign_in.this, "Invalid Credential(s)", Toast.LENGTH_LONG).show();
                            dilalog.dismiss();
                            return;
                        } else if (!newemail.getText().toString().contains("@bml.edu.in")) {
                            Toast.makeText(Sign_in.this, "Please enter college email id", Toast.LENGTH_LONG).show();
                            dilalog.dismiss();
                            return;
                        } else if (newpass.getText().toString().length() < 6 || newpass.getText().toString().length() > 8) {

                            Toast.makeText(Sign_in.this, "Password length should be between 6 to 8 letters", Toast.LENGTH_LONG).show();
                            dilalog.dismiss();
                            return;
                        } else if (!newpassconfirm.getText().toString().equals(newpass.getText().toString())) {
                            newpassconfirm.setError("Password didn't match");
                            dilalog.dismiss();
                            return;
                        }

                        else {

                            final User user = new User(newuser.getText().toString(), newpass.getText().toString(), newemail.getText().toString());
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child(user.getUser_name()).exists()) {
                                        Toast.makeText(Sign_in.this, "Already Registered", Toast.LENGTH_LONG).show();
                                        dilalog.dismiss();

                                        return;

                                    } else {
                                        Add_user();
                                        databaseReference.child(user.getUser_name()).setValue(user);
                                        Toast.makeText(Sign_in.this, "Signup successful", Toast.LENGTH_LONG).show();
                                        dilalog.dismiss();
                                        mdialog.dismiss();


                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                    Log.e("ERRor", databaseError.toString());
                                    dilalog.dismiss();


                                }
                            });


                        }

                    }
                });
                Button c = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                c.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                               mdialog.dismiss();

                    }
                });


            }
        });
        mdialog.show();
    }







    private void Add_user() {

        mAuth.createUserWithEmailAndPassword(newemail.getText().toString(),newpass.getText().toString())
                .addOnCompleteListener(Sign_in.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e("user created", "createUserWithEmail:success");
                            FirebaseUser userfb = mAuth.getCurrentUser();
                            userfb.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.e("mail sent", "Email sent.");
                                                Toast.makeText(Sign_in.this,"Signup Successful",Toast.LENGTH_LONG).show();
                                                Toast.makeText(Sign_in.this,"Email Verification Sent",Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });


                        } else {
                            Log.e("email",newemail.getText().toString());
                            FirebaseAuthException e = (FirebaseAuthException )task.getException();
                            Toast.makeText(Sign_in.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();

                            Log.e("exception",e.toString());

                        }

                        // ...
                    }
                });
    }

    //remembers user
    public void savepref()
    {

        SharedPreferences s = getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();
        editor.putString(user.getText().toString(),PREF_UNAME);
        editor.putString(pass.getText().toString(),PREF_PASSWORD);
        editor.apply();
    }
    public void loadpref()
    {
        sharedpreferences = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(PREF_UNAME)) {
            user.setText(sharedpreferences.getString(PREF_UNAME, DefaultUnameValue));
        }
        if (sharedpreferences.contains(PREF_PASSWORD)) {
            pass.setText(sharedpreferences.getString(PREF_PASSWORD, DefaultPasswordValue));

        }

    }


    @Override
    public void onPause() {
        super.onPause();
        savepref();

    }

    @Override
    protected void onStart() {
        super.onStart();



    }

    @Override
    public void onResume() {

        super.onResume();
        loadpref();
    }
}

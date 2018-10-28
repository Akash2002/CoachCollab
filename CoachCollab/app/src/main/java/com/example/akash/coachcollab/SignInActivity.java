package com.example.akash.coachcollab;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button loginButton;
    private TextView createAccountTextView;
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        firebaseAuth = FirebaseAuth.getInstance();
        loginButton = findViewById(R.id.loginButton);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        createAccountTextView = findViewById(R.id.createAccountTextView);

        loginInUser();

        createAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignInActivity.this, CreateAccountActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            FirebaseDatabase.getInstance().getReference().child("Student").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot d: dataSnapshot.getChildren()){
                        if(d.getKey().equals(user.getEmail().substring(0,user.getEmail().indexOf("@")))){
                            updateStudentUI(user);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            FirebaseDatabase.getInstance().getReference().child("Teacher").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot d: dataSnapshot.getChildren()){
                        if(d.getKey().equals(user.getEmail().substring(0,user.getEmail().indexOf("@")))){
                            updateTeacherUI(user);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {

    }

    public void updateTeacherUI(FirebaseUser user){
        Intent i = new Intent(SignInActivity.this, MainActivity.class);
        i.putExtra("emailAddress",user.getEmail());
        i.putExtra("FromWhere","SignIn");
        startActivity(i);
    }

    public void updateStudentUI(FirebaseUser user){
        Intent i = new Intent(getApplicationContext(), StudentMainActivity.class);
        i.putExtra("FromWhere","SignIn");
        startActivity(i);
    }

    public void loginInUser(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailString = emailEditText.getText().toString();
                final String passwordString = passwordEditText.getText().toString();
                if(emailString.length() > 1 && passwordString.length() > 1) {
                    Task task = firebaseAuth.signInWithEmailAndPassword(emailString, passwordString);
                    task.addOnCompleteListener(SignInActivity.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                FirebaseDatabase.getInstance().getReference().child("Teacher").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot d: dataSnapshot.getChildren()){
                                            if(d.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail().substring(0,FirebaseAuth.getInstance().getCurrentUser().getEmail().indexOf("@")))){
                                                updateTeacherUI(FirebaseAuth.getInstance().getCurrentUser());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                FirebaseDatabase.getInstance().getReference().child("Student").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot d: dataSnapshot.getChildren()){
                                            if(d.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail().substring(0,FirebaseAuth.getInstance().getCurrentUser().getEmail().indexOf("@")))){
                                                updateStudentUI(FirebaseAuth.getInstance().getCurrentUser());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                            } else {
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

}

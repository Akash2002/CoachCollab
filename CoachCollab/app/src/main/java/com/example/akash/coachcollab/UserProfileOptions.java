package com.example.akash.coachcollab;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfileOptions extends AppCompatActivity {

    private TextView userEmailIDTextView;
    private EditText descriptionEditText;
    private Button saveButton;
    private static String descriptionVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_options);

        userEmailIDTextView = findViewById(R.id.userEmailID);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        saveButton = findViewById(R.id.saveButton);

        userEmailIDTextView.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        descriptionEditText.setText(descriptionVal);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = descriptionEditText.getText().toString();
                if(description.length() > 50){
                    FirebaseDatabase.getInstance().getReference().child(MainActivity.firebaseUserHeader).child("Description").setValue(description);
                    Snackbar.make(findViewById(R.id.mainView), "Saved Data.", BaseTransientBottomBar.LENGTH_SHORT).show();
                    FirebaseDatabase.getInstance().getReference().child("Teacher").child(MainActivity.firebaseUserHeader).child("Description").setValue(description);
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.putExtra("FromWhere","UserProfileOptions");
                    startActivity(i);
                } else if (description.length() < 50 && description.length() > 0){
                    final Snackbar snackbar = Snackbar.make(findViewById(R.id.mainView), "Please describe yourself more", BaseTransientBottomBar.LENGTH_LONG);
                    snackbar.setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
                    snackbar.show();
                } else if (description.length() == 0){
                    final Snackbar snackbar = Snackbar.make(findViewById(R.id.mainView), "Please write something about yourself", BaseTransientBottomBar.LENGTH_LONG);
                    snackbar.setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
                    snackbar.show();
                }
            }
        });
    }
    public static void passDescriptionVal(String description){
        descriptionVal = description;
    }
}

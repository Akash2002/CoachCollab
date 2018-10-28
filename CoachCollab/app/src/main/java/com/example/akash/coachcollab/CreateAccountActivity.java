package com.example.akash.coachcollab;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText fullNameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText rePasswordInput;
    private RadioGroup chooseUserRadioGroup;
    private Button createAccountButton;
    private RadioButton radioButton;
    private String errorMessage;
    private TextView signInTextView;
    private ProgressBar progressBar;
    private boolean continueOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        fullNameInput = findViewById(R.id.nameEditText);
        emailInput = findViewById(R.id.emailEditText);
        passwordInput = findViewById(R.id.passwordEditText);
        rePasswordInput = findViewById(R.id.rePasswordEditText);
        chooseUserRadioGroup = findViewById(R.id.chooseUserGroup);
        createAccountButton = findViewById(R.id.createAccountButton);
        signInTextView = findViewById(R.id.signInText);
        progressBar = findViewById(R.id.progressBar2);

        progressBar.setVisibility(View.INVISIBLE);

        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateAccountActivity.this, SignInActivity.class));
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                if(!(emailInput.getText().toString().length() > 0)) {
                    errorMessage = "Please enter email.";
                    continueOn = false;
                }
                else if(!(passwordInput.getText().toString().length() > 0)) {
                    errorMessage = "Please enter password.";
                    continueOn = false;
                }
                else if(!(rePasswordInput.getText().toString().length() > 0)) {
                    errorMessage = "Please reenter password.";
                    continueOn = false;
                }
                else if(!(fullNameInput.getText().toString().length() > 0)) {
                    errorMessage = "Please enter name.";
                    continueOn = false;
                }
                else if(!(InputChecker.checkEmail(emailInput.getText().toString()))) {
                    errorMessage = InputChecker.EMAIL_ERROR_MESSAGE;
                    continueOn = false;
                }
                else if(!(InputChecker.checkPassword(passwordInput.getText().toString(),rePasswordInput.getText().toString()))) {
                    errorMessage = InputChecker.PASSWORD_ERROR_MESSAGE;
                    continueOn = false;
                } else {
                    continueOn = true;
                }

                if(continueOn == false){
                    AlertDialog.Builder alert = new AlertDialog.Builder(CreateAccountActivity.this);
                    alert.setTitle("Error");
                    alert.setMessage(errorMessage);
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    alert.create();
                    alert.show();
                }

                if(continueOn){
                    int radioID = chooseUserRadioGroup.getCheckedRadioButtonId();
                    radioButton = findViewById(radioID);
                    final String userType = radioButton.getText().toString();
                    final String email = emailInput.getText().toString();
                    final String password = passwordInput.getText().toString();
                    final String fullName = fullNameInput.getText().toString();

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                if(userType.equals("Teacher")) {
                                    Intent i = new Intent(CreateAccountActivity.this, MainActivity.class);
                                    i.putExtra("FromWhere", "CreateAccount");
                                    i.putExtra("Email", email);
                                    i.putExtra("Name", fullName);
                                    i.putExtra("UserType", userType);
                                    startActivity(i);
                                } else if(userType.equals("Student")){
                                    Intent i = new Intent(CreateAccountActivity.this, StudentMainActivity.class);
                                    i.putExtra("FromWhere", "CreateAccount");
                                    i.putExtra("Email", email);
                                    i.putExtra("Name", fullName);
                                    i.putExtra("UserType", userType);
                                    startActivity(i);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }

            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}

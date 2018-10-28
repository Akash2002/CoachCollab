package com.example.akash.coachcollab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentDetailsActivity extends AppCompatActivity {

    public static String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        findViewById(R.id.profileConstraintLayout).requestFocus();

        description = getIntent().getStringExtra("Description");
        String email = getIntent().getStringExtra("Email");
        String gradeLevel = getIntent().getStringExtra("Grade Level");
        String name = getIntent().getStringExtra("Name");
        String rating = getIntent().getStringExtra("Rating");
        String subjects = getIntent().getStringExtra("Subjects");

        TextView nameTextView = findViewById(R.id.fullNameTextView);
        TextView emailTextView = findViewById(R.id.emailTextView);

        nameTextView.setText(name);
        emailTextView.setText(email);

        RecyclerItem descriptionRecyclerItem = new RecyclerItem("Description",format(description));
        RecyclerItem gradeLevelRecyclerItem = new RecyclerItem("Grade Level",gradeLevel);
        RecyclerItem ratingRecyclerItem = new RecyclerItem("Rating",rating);
        RecyclerItem subjectsRecyclerItem = new RecyclerItem("Subject(s)",subjects);

        ArrayList<RecyclerItem> recyclerItemArrayList = new ArrayList<>();
        recyclerItemArrayList.add(descriptionRecyclerItem);
        recyclerItemArrayList.add(gradeLevelRecyclerItem);
        recyclerItemArrayList.add(subjectsRecyclerItem);
        recyclerItemArrayList.add(ratingRecyclerItem);

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(recyclerItemArrayList, StudentDetailsActivity.this, false, false);

        RecyclerView userDetailsRecyclerView = findViewById(R.id.userDetailsRecyclerView);
        userDetailsRecyclerView.setHasFixedSize(false);
        userDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        userDetailsRecyclerView.setAdapter(recyclerViewAdapter);

    }
    private String format(String string){
        if(string.length() < 30){
            return string;
        } else {
            return string.substring(0,31) + "...";
        }
    }
}

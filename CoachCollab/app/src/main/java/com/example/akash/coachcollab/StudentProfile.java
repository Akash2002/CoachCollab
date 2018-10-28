package com.example.akash.coachcollab;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class StudentProfile extends Fragment {

    private TextView studentNameTextView;
    private TextView descriptionTextView;
    private DatabaseReference databaseReference;
    public static String studentFirebaseHeader;
    private StudentUser user;
    private RecyclerView studentProfileOptionsRecyclerView;
    private ArrayList<RecyclerItem> recyclerItemsArrayList;
    private ConstraintLayout profileLayout;
    private Button logoutButton;

    public StudentProfile() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_student_profile, container, false);
        StudentMainActivity.toolbar.setTitle("Profile");
        studentFirebaseHeader = FirebaseAuth.getInstance().getCurrentUser().getEmail().substring(0, FirebaseAuth.getInstance().getCurrentUser().getEmail().indexOf("@"));
        user = new StudentUser();
        studentNameTextView = v.findViewById(R.id.studentNameTextView);
        descriptionTextView = v.findViewById(R.id.studentDescriptionTextView);
        studentProfileOptionsRecyclerView = v.findViewById(R.id.studentProfileOptions);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Student").child(studentFirebaseHeader);
        recyclerItemsArrayList = new ArrayList<>();
        profileLayout = v.findViewById(R.id.studentProfileConstraintLayout);
        logoutButton = v.findViewById(R.id.logoutButton);
        TextView email = v.findViewById(R.id.studentEmailTextview);
        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View v = LayoutInflater.from(getActivity()).inflate(R.layout.student_profile_card_alert_layout, null);
                final EditText descEditText = v.findViewById(R.id.descriptionEditText);
                Button saveButton = v.findViewById(R.id.saveButton);
                    databaseReference.child("Description").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            descEditText.setText(dataSnapshot.getValue().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setView(v);
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!(descEditText.getText().toString().length() < 25)) {
                            FirebaseDatabase.getInstance().getReference().child("Student").child(StudentProfile.studentFirebaseHeader).child("Description").setValue(descEditText.getText().toString());
                        }
                    }
                });
                alert.create().show();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), SignInActivity.class));
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> data = (Map) dataSnapshot.getValue();
                user.setDescription(data.get("Description"));
                user.setEmail(data.get("Email"));
                user.setName(data.get("Name"));
                user.setRating(String.valueOf(data.get("Rating")));
                user.setType(data.get("Type"));
                user.setSubject(String.valueOf(data.get("Subjects")));
                user.setGradeLevel(String.valueOf(data.get("Grade Level")));
                studentNameTextView.setText(data.get("Name"));
                if(user.getDescription().length() <= 100){
                    descriptionTextView.setText(user.getDescription());
                } else {
                    descriptionTextView.setText(user.getDescription().substring(0,101) + "...");
                }
                Log.i("Subjects",data.toString());
                setRecyclerViewWithData(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return v;
    }

    public void setRecyclerViewWithData(StudentUser user){
        recyclerItemsArrayList.clear();
        System.out.println("Subjects " + user.getSubject());
        RecyclerItem subjectItem = new RecyclerItem("Subjects",user.getSubject());
        RecyclerItem gradeItem = new RecyclerItem("Grade Level", user.getGradeLevel());
        RecyclerItem ratingItem = new RecyclerItem("Rating", user.getRating());
        recyclerItemsArrayList.add(subjectItem);
        recyclerItemsArrayList.add(gradeItem);
        recyclerItemsArrayList.add(ratingItem);
        studentProfileOptionsRecyclerView.setHasFixedSize(true);
        studentProfileOptionsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        StudentRecyclerAdapter recyclerAdapter = new StudentRecyclerAdapter(recyclerItemsArrayList, getActivity());
        studentProfileOptionsRecyclerView.setAdapter(recyclerAdapter);
    }

}

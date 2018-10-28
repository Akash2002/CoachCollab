package com.example.akash.coachcollab;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private String emailString;
    private TextView profileTextView;
    private Button logoutButton;

    private RecyclerView timePrefRecyclerView;
    private RecyclerViewAdapter timePrefRecyclerViewAdapter;
    private ArrayList<RecyclerItem> recyclerItemsArrayList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private RecyclerView teachingRecyclerView;
    private RecyclerViewAdapter teachingRecyclerViewAdapter;
    private ArrayList<RecyclerItem> teachingArrayList;

    public static String userDisplayName;
    private ConstraintLayout profileLayout;

    private RecyclerView otherOptionsRecyclerView;
    private ArrayList<RecyclerItem> otherOptionsRecyclerItemsArrayList;
    private RecyclerViewAdapter otherOptionsRecyclerViewAdapter;

    private TextView descriptionTextView;
    public static Activity activity;
    private TextView userEmailTextView;
    public static String firebaseUserHeader;

    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        MainActivity.toolbar.setTitle("Profile");
        logoutButton = v.findViewById(R.id.logoutButton);
        profileTextView = v.findViewById(R.id.profileTextView);
        timePrefRecyclerView = v.findViewById(R.id.userProfileOptions);
        profileLayout = v.findViewById(R.id.profileLayout);
        userEmailTextView = v.findViewById(R.id.emailTextView);
        otherOptionsRecyclerView = v.findViewById(R.id.otherUserOptions);
        teachingRecyclerView = v.findViewById(R.id.teachingRecyclerView);
        activity = getActivity();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Teacher");
        recyclerItemsArrayList = new ArrayList<>();
        otherOptionsRecyclerItemsArrayList = new ArrayList<>();
        teachingArrayList = new ArrayList<>();
        descriptionTextView = v.findViewById(R.id.descriptionProfileTextView);
        userEmailTextView.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        userDisplayName = FirebaseAuth.getInstance().getCurrentUser().getEmail().substring(0,FirebaseAuth.getInstance().getCurrentUser().getEmail().indexOf("@"));

        firebaseUserHeader = FirebaseAuth.getInstance().getCurrentUser().getEmail().substring(0, FirebaseAuth.getInstance().getCurrentUser().getEmail().indexOf("@"));
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), SignInActivity.class));
            }
        });

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View v = LayoutInflater.from(getActivity()).inflate(R.layout.student_profile_card_alert_layout, null);
                final EditText descEditText = v.findViewById(R.id.descriptionEditText);
                Button saveButton = v.findViewById(R.id.saveButton);
                FirebaseDatabase.getInstance().getReference().child("Teacher").child(firebaseUserHeader).child("Description").addValueEventListener(new ValueEventListener() {
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
                        if(!(descEditText.getText().toString().length() < 25)){
                            FirebaseDatabase.getInstance().getReference().child("Teacher").child(firebaseUserHeader).child("Description").setValue(descEditText.getText().toString());
                        }
                    }
                });
                alert.create().show();
            }
        });

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren()){
                    if(d.getKey().equals(MainActivity.firebaseUserHeader)){
                        getUserDetails(d.getKey().toString(), databaseReference);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        databaseReference.addValueEventListener(valueEventListener);

        return v;
    }

    public void getUserDetails(String userDisplayName, final DatabaseReference databaseReference){
        final TeacherUser teacherUser = new TeacherUser();
        teacherUser.setUserDisplayName(userDisplayName);
        databaseReference.child(teacherUser.getUserDisplayName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> data = (Map) dataSnapshot.getValue();
                teacherUser.setUserEmail(data.get("Email"));
                teacherUser.setUserName(data.get("Name"));
                teacherUser.setUserFromTime(String.valueOf(data.get("FromTime")));
                teacherUser.setUserToTime(String.valueOf(data.get("ToTime")));
                teacherUser.setUserLocation(data.get("Location"));
                teacherUser.setDate(data.get("Date"));
                teacherUser.setUserDescription(data.get("Description"));
                teacherUser.setRating(String.valueOf(data.get("Rating")));
                teacherUser.setSubjectMap(String.valueOf(data.get("Subjects")));
                teacherUser.setGradeLevel(String.valueOf(data.get("GradeLevel")));
                descriptionTextView.setText(modifyDescription(teacherUser.getUserDescription()));
                profileTextView.setText(teacherUser.getUserName());
                UserProfileOptions.passDescriptionVal(teacherUser.getUserDescription());
                showRecyclerTimeOptions(teacherUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void showRecyclerTimeOptions(TeacherUser user){
        String subjectList = "";
        recyclerItemsArrayList.clear();
        teachingArrayList.clear();
        otherOptionsRecyclerItemsArrayList.clear();
        timePrefRecyclerView.setHasFixedSize(true);
        timePrefRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        otherOptionsRecyclerView.setHasFixedSize(true);
        otherOptionsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerItem fromTimeItem = new RecyclerItem("From Time", user.getUserFromTime());
        RecyclerItem toTimeItem = new RecyclerItem("To Time", user.getUserToTime());
        RecyclerItem date = new RecyclerItem("Date", user.getDate());
        recyclerItemsArrayList.add(fromTimeItem);
        recyclerItemsArrayList.add(toTimeItem);
        recyclerItemsArrayList.add(date);
        RecyclerItem locationItem = new RecyclerItem("Location", user.getUserLocation());
        RecyclerItem rating = new RecyclerItem("Rating", user.getRating());
        otherOptionsRecyclerItemsArrayList.add(locationItem);
        otherOptionsRecyclerItemsArrayList.add(rating);
        timePrefRecyclerViewAdapter = new RecyclerViewAdapter(recyclerItemsArrayList, getActivity(), true, false);
        timePrefRecyclerViewAdapter.setFragmentManager(getActivity().getFragmentManager());
        timePrefRecyclerView.setAdapter(timePrefRecyclerViewAdapter);
        otherOptionsRecyclerViewAdapter = new RecyclerViewAdapter(otherOptionsRecyclerItemsArrayList, getActivity(), true, false);
        otherOptionsRecyclerViewAdapter.setFragmentManager(getActivity().getFragmentManager());
        otherOptionsRecyclerView.setAdapter(otherOptionsRecyclerViewAdapter);
        RecyclerItem subjects = new RecyclerItem("Subjects", user.getSubjectMap());
        RecyclerItem gradeLevel = new RecyclerItem("Grade Level", user.getGradeLevel());
        teachingArrayList.add(subjects);
        teachingArrayList.add(gradeLevel);
        teachingRecyclerView.setHasFixedSize(true);
        teachingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        teachingRecyclerViewAdapter = new RecyclerViewAdapter(teachingArrayList, getActivity(), true, false);
        teachingRecyclerViewAdapter.setFragmentManager(getActivity().getFragmentManager());
        teachingRecyclerView.setAdapter(teachingRecyclerViewAdapter);
    }

    private String modifyDescription(String description){
        if(description.length() <= 100){
            return description;
        } else if (description.length() >= 100){
            return description.substring(0,101) + "...";
        }
        return "";
    }
}

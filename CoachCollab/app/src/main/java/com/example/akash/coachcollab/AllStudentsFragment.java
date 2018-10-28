package com.example.akash.coachcollab;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class AllStudentsFragment extends Fragment {

    private RecyclerView allStudentsRecyclerView;
    private ArrayList<String> allStudentFirebaseHeaders;
    private ArrayList<String> allStudentGrades;
    private ArrayList<String> allStudentSubjects;
    private ArrayList<UserRecyclerItem> allStudentsRecyclerArrayList;
    private ArrayList<StudentUser> studentUserArrayList;
    int counter = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_all_students, container, false);
        MainActivity.toolbar.setTitle("Students");

        allStudentsRecyclerView = v.findViewById(R.id.allStudentsRecyclerView);
        allStudentGrades = new ArrayList<>();
        allStudentFirebaseHeaders = new ArrayList<>();
        allStudentSubjects = new ArrayList<>();
        allStudentsRecyclerArrayList = new ArrayList<>();
        studentUserArrayList = new ArrayList<>();


        FirebaseDatabase.getInstance().getReference().child("Student").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren()) {
                    allStudentFirebaseHeaders.add(d.getKey());
                    if (allStudentFirebaseHeaders.size() == dataSnapshot.getChildrenCount()) {
                        for (int i = 0; i < allStudentFirebaseHeaders.size(); i++) {
                            FirebaseDatabase.getInstance().getReference().child("Student").child(allStudentFirebaseHeaders.get(i)).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Map<String, String> data = (Map) dataSnapshot.getValue();
                                    allStudentGrades.add(data.get("Grade Level"));
                                    allStudentSubjects.add(data.get("Subjects"));
                                    StudentUser studentUser = new StudentUser(
                                            String.valueOf(data.get("Grade Level")),
                                            String.valueOf(data.get("Subjects")),
                                            data.get("Description"),
                                            data.get("Email"),
                                            data.get("Name"),
                                            String.valueOf(data.get("Rating")),
                                            data.get("Type")
                                    );
                                    studentUserArrayList.add(studentUser);
                                    if(allStudentFirebaseHeaders.size() == allStudentGrades.size() && allStudentGrades.size() == allStudentSubjects.size())
                                        for(int i = 0; i < allStudentFirebaseHeaders.size(); i++){
                                            UserRecyclerItem userRecyclerItem = new UserRecyclerItem(formatText(studentUserArrayList.get(i).getName()), formatText(studentUserArrayList.get(i).getSubject()), "[" + studentUserArrayList.get(i).getGradeLevel() + "]");
                                            allStudentsRecyclerArrayList.add(userRecyclerItem);
                                            AllUsersRecyclerAdapter allUsersRecyclerAdapter = new AllUsersRecyclerAdapter(allStudentsRecyclerArrayList, true);
                                            allUsersRecyclerAdapter.setStudentUserArrayList(studentUserArrayList);
                                            allStudentsRecyclerView.setHasFixedSize(false);
                                            allStudentsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                            allStudentsRecyclerView.setAdapter(allUsersRecyclerAdapter);
                                        }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;
    }

    public String formatText(String string){
        if(string != null) {
            if (string.length() > 15) {
                return string.substring(0, 16) + "...";
            }
        }
        return string;
    }

}

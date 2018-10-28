package com.example.akash.coachcollab;


import android.app.AlertDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RatingBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentAllUsers extends Fragment {

    ArrayList<String> allTeacherFirebaseHeaders = new ArrayList<>();
    ArrayList<TeacherUser> teacherUserArrayList = new ArrayList<>();
    boolean changeRating = false;
    RecyclerView allTeachersRecyclerView;

    public StudentAllUsers() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_student_all_users, container, false);
        setHasOptionsMenu(true);
        StudentMainActivity.toolbar.setTitle("Profile");
        allTeachersRecyclerView = v.findViewById(R.id.allTeachersRecyclerView);
        FirebaseDatabase.getInstance().getReference().child("Teacher").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren()) {
                    allTeacherFirebaseHeaders.add(d.getKey());
                    if (allTeacherFirebaseHeaders.size() == dataSnapshot.getChildrenCount()) {
                        for (int i = 0; i < allTeacherFirebaseHeaders.size(); i++) {
                            final int j = i;
                            FirebaseDatabase.getInstance().getReference().child("Teacher").child(allTeacherFirebaseHeaders.get(i)).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Map<String, String> data = (Map) dataSnapshot.getValue();
                                    ArrayList<UserRecyclerItem> userRecyclerItemArrayList = new ArrayList<>();
                                    TeacherUser teacher = new TeacherUser(
                                            String.valueOf(data.get("Subjects")),
                                            String.valueOf(data.get("GradeLevel")),
                                            data.get("Name"),
                                            data.get("Email"),
                                            String.valueOf(data.get("FromTime")),
                                            String.valueOf(data.get("ToTime")),
                                            data.get("Location"),
                                            data.get(allTeacherFirebaseHeaders.get(j)),
                                            String.valueOf(data.get("Date")),
                                            data.get("Description"),
                                            data.get("Availability"),
                                            String.valueOf(data.get("Rating"))
                                    );
                                    teacherUserArrayList.add(teacher);
                                    if(allTeacherFirebaseHeaders.size() == teacherUserArrayList.size())
                                        for(int i = 0; i < allTeacherFirebaseHeaders.size(); i++){
                                            UserRecyclerItem userRecyclerItem = new UserRecyclerItem(formatText(teacherUserArrayList.get(i).getUserName()), teacherUserArrayList.get(i).getSubjectMap(), formatText(teacherUserArrayList.get(i).getGradeLevel()));
                                            userRecyclerItemArrayList.add(userRecyclerItem);
                                            AllUsersRecyclerAdapter allUsersRecyclerAdapter = new AllUsersRecyclerAdapter(userRecyclerItemArrayList, false);
                                            allUsersRecyclerAdapter.setTeacherUserArrayList(teacherUserArrayList);
                                            allTeachersRecyclerView.setHasFixedSize(false);
                                            allTeachersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                            allTeachersRecyclerView.setAdapter(allUsersRecyclerAdapter);
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
        if(string.length() > 15){
            return string.substring(0,16) + "...";
        }
        return string;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
        MenuItem filterItem = menu.findItem(R.id.filterUsers);

        filterItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                final View filterAlertView = LayoutInflater.from(getActivity()).inflate(R.layout.filter_alert_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(filterAlertView);
                final AlertDialog alertDialog = builder.create();
                Button filterButton = filterAlertView.findViewById(R.id.filterAlertButton);
                filterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CheckBox elementary = filterAlertView.findViewById(R.id.elementaryCheckBox);
                        CheckBox middle = filterAlertView.findViewById(R.id.middleCheckBox);
                        CheckBox high = filterAlertView.findViewById(R.id.highCheckBox);
                        CheckBox math = filterAlertView.findViewById(R.id.mathCheckBox);
                        CheckBox science = filterAlertView.findViewById(R.id.scienceCheckBox);
                        CheckBox social = filterAlertView.findViewById(R.id.socialCheckBox);
                        CheckBox english = filterAlertView.findViewById(R.id.englishCheckBox);
                        CheckBox computerScience = filterAlertView.findViewById(R.id.computerCheckBox);
                        RatingBar minRatingBar = filterAlertView.findViewById(R.id.minRatingBar);

                        ArrayList<String> gradeLevelArrayList = new ArrayList<>();
                        ArrayList<String> subjectLevelArrayList = new ArrayList<>();

                        float minRating = minRatingBar.getRating();

                        minRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                            @Override
                            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                                changeRating = b;
                            }
                        });

                        if(elementary.isChecked()){
                            gradeLevelArrayList.add(elementary.getText().toString());
                        }
                        if(middle.isChecked()){
                            gradeLevelArrayList.add(middle.getText().toString());
                        }
                        if(high.isChecked()){
                            gradeLevelArrayList.add(high.getText().toString());
                        }
                        if(math.isChecked()){
                            subjectLevelArrayList.add(math.getText().toString());
                        }
                        if(science.isChecked()){
                            subjectLevelArrayList.add(science.getText().toString());
                        }
                        if(english.isChecked()){
                            subjectLevelArrayList.add(english.getText().toString());
                        }
                        if(social.isChecked()){
                            subjectLevelArrayList.add(social.getText().toString());
                        }
                        if(computerScience.isChecked()){
                            subjectLevelArrayList.add(computerScience.getText().toString());
                        }

                        showSearchData(subjectLevelArrayList, gradeLevelArrayList, changeRating);

                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    public void showSearchData(final ArrayList<String> subjects, final ArrayList<String> grades, final boolean changeRating){
        final ArrayList<TeacherUser> teacherUserArraylist = new ArrayList<>();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Teacher");

        final boolean checkSubjects = subjects.size() > 0 ? true:false;
        final boolean checkGrades = grades.size() > 0 ? true:false;

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<String> users = new ArrayList<>();
                final ArrayList<UserRecyclerItem> userRecyclerItemArrayList = new ArrayList<>();
                for(DataSnapshot d: dataSnapshot.getChildren()){
                    users.add(d.getKey());
                    if(users.size() == dataSnapshot.getChildrenCount()) {
                        for (int i = 0; i < users.size(); i++) {
                            final int j = i;
                            databaseReference.child(users.get(i)).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    final Map map = (Map) dataSnapshot.getValue();
                                    if(checkSubjects && checkGrades && !changeRating) {
                                        if (map.get("GradeLevel") != null || map.get("GradeLevel") != "null" && map.get("Subjects") != null || map.get("Subjects") != "null") {
                                            databaseReference.child(users.get(j)).child("GradeLevel").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    final ArrayList data = (ArrayList) dataSnapshot.getValue();
                                                    databaseReference.child(users.get(j)).child("Subjects").addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            final ArrayList data2 = (ArrayList) dataSnapshot.getValue();
                                                            if (data.equals(grades) && data2.equals(subjects)) {
                                                                Log.i("Print", "YEs");
                                                                databaseReference.child(users.get(j)).addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        Map<String, String> data = (Map) dataSnapshot.getValue();
                                                                        Log.i("Data", data.toString());
                                                                        TeacherUser teacher = new TeacherUser(
                                                                                String.valueOf(data.get("Subjects")),
                                                                                String.valueOf(data.get("GradeLevel")),
                                                                                data.get("Name"),
                                                                                data.get("Email"),
                                                                                data.get("FromTime"),
                                                                                data.get("ToTime"),
                                                                                data.get("Location"),
                                                                                data.get(allTeacherFirebaseHeaders.get(j)),
                                                                                data.get("Date"),
                                                                                data.get("Description"),
                                                                                data.get("Availability"),
                                                                                String.valueOf(data.get("Rating"))
                                                                        );
                                                                        teacherUserArraylist.add(teacher);
                                                                        UserRecyclerItem userRecyclerItem = new UserRecyclerItem(teacher.getUserName(), formatText(teacher.getSubjectMap()), formatText(teacher.getGradeLevel()));
                                                                        userRecyclerItemArrayList.add(userRecyclerItem);
                                                                        AllUsersRecyclerAdapter allUsersRecyclerAdapter = new AllUsersRecyclerAdapter(userRecyclerItemArrayList, false);
                                                                        allUsersRecyclerAdapter.setTeacherUserArrayList(teacherUserArrayList);
                                                                        allTeachersRecyclerView.setHasFixedSize(false);
                                                                        allTeachersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                                                        allTeachersRecyclerView.setAdapter(allUsersRecyclerAdapter);
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    } else if (checkSubjects == true && checkGrades == false && !changeRating){
                                        if (map.get("Subjects") != null || map.get("Subjects") != "null") {
                                            databaseReference.child(users.get(j)).child("Subjects").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    final ArrayList data2 = (ArrayList) dataSnapshot.getValue();
                                                    if (data2.equals(subjects)) {
                                                        Log.i("Print", "YEs");
                                                        databaseReference.child(users.get(j)).addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                Map<String, String> data = (Map) dataSnapshot.getValue();
                                                                Log.i("Data", data.toString());
                                                                TeacherUser teacher = new TeacherUser(
                                                                        String.valueOf(data.get("Subjects")),
                                                                        String.valueOf(data.get("GradeLevel")),
                                                                        data.get("Name"),
                                                                        data.get("Email"),
                                                                        data.get("FromTime"),
                                                                        data.get("ToTime"),
                                                                        data.get("Location"),
                                                                        data.get(allTeacherFirebaseHeaders.get(j)),
                                                                        data.get("Date"),
                                                                        data.get("Description"),
                                                                        data.get("Availability"),
                                                                        String.valueOf(data.get("Rating"))
                                                                );
                                                                teacherUserArraylist.add(teacher);
                                                                UserRecyclerItem userRecyclerItem = new UserRecyclerItem(teacher.getUserName(), formatText(teacher.getSubjectMap()), formatText(teacher.getGradeLevel()));
                                                                userRecyclerItemArrayList.add(userRecyclerItem);
                                                                AllUsersRecyclerAdapter allUsersRecyclerAdapter = new AllUsersRecyclerAdapter(userRecyclerItemArrayList, false);
                                                                allUsersRecyclerAdapter.setTeacherUserArrayList(teacherUserArrayList);
                                                                allTeachersRecyclerView.setHasFixedSize(false);
                                                                allTeachersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                                                allTeachersRecyclerView.setAdapter(allUsersRecyclerAdapter);
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    } else if (checkSubjects == false && checkGrades == true && !changeRating){
                                        if (map.get("GradeLevel") != null || map.get("GradeLevel") != "null") {
                                            databaseReference.child(users.get(j)).child("GradeLevel").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    final ArrayList data = (ArrayList) dataSnapshot.getValue();
                                                    if (data.equals(grades)) {
                                                        databaseReference.child(users.get(j)).addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                Map<String, String> data = (Map) dataSnapshot.getValue();
                                                                Log.i("Data", data.toString());
                                                                TeacherUser teacher = new TeacherUser(
                                                                        String.valueOf(data.get("Subjects")),
                                                                        String.valueOf(data.get("GradeLevel")),
                                                                        data.get("Name"),
                                                                        data.get("Email"),
                                                                        data.get("FromTime"),
                                                                        data.get("ToTime"),
                                                                        data.get("Location"),
                                                                        data.get(allTeacherFirebaseHeaders.get(j)),
                                                                        data.get("Date"),
                                                                        data.get("Description"),
                                                                        data.get("Availability"),
                                                                        String.valueOf(data.get("Rating"))
                                                                );
                                                                teacherUserArraylist.add(teacher);
                                                                UserRecyclerItem userRecyclerItem = new UserRecyclerItem(teacher.getUserName(), formatText(teacher.getSubjectMap()), formatText(teacher.getGradeLevel()));
                                                                userRecyclerItemArrayList.add(userRecyclerItem);
                                                                AllUsersRecyclerAdapter allUsersRecyclerAdapter = new AllUsersRecyclerAdapter(userRecyclerItemArrayList, false);
                                                                allUsersRecyclerAdapter.setTeacherUserArrayList(teacherUserArrayList);
                                                                allTeachersRecyclerView.setHasFixedSize(false);
                                                                allTeachersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                                                allTeachersRecyclerView.setAdapter(allUsersRecyclerAdapter);
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }
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
    }
}
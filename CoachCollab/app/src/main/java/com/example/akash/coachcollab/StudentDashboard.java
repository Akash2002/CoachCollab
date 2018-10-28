package com.example.akash.coachcollab;

import android.content.Context;
import android.graphics.Canvas;
import android.icu.util.Freezable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class StudentDashboard extends Fragment {

    private RecyclerView requestsRecyclerView;
    private ArrayList<String> requestToTeacherNameArrayList;
    public static String firebaseUserHeader;
    private RecyclerView upcomingRecyclerView;
    final ArrayList<String> teacherScheduleArrayList = new ArrayList<>();
    final ArrayList<TeacherTimeManager> teacherTimeManagerArrayList= new ArrayList<>();
    private RecyclerView ratingRecyclerView;
    public static boolean hasSubmit = false;
    ScheduleRecyclerAdapter scheduleRecyclerAdapter;
    StudentRequestRecyclerAdapter studentRequestRecyclerAdapter;
    ArrayList<String> statusArrayList = new ArrayList<>();
    ArrayList<String> teacherRequested = new ArrayList<>();

    int c = 0;

    public StudentDashboard(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_student_dashboard, container, false);
        requestsRecyclerView = v.findViewById(R.id.requestsRecyclerView);
        StudentMainActivity.toolbar.setTitle("Dashboard");
        requestToTeacherNameArrayList = new ArrayList<>();
        firebaseUserHeader = FirebaseAuth.getInstance().getCurrentUser().getEmail().substring(0, FirebaseAuth.getInstance().getCurrentUser().getEmail().indexOf("@"));
        upcomingRecyclerView = v.findViewById(R.id.upcoming_student_recycler_view);
        ratingRecyclerView = v.findViewById(R.id.ratingRecyclerView);

        showRequests();
        showScheduleData();
        showRatingRecyclerView();
        return v;
    }

    public void showRequests(){
        FirebaseDatabase.getInstance().getReference().child("Student").child(firebaseUserHeader).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                statusArrayList.clear();
                teacherRequested.clear();
                Log.i("Running0", "I am running");
                Map <String, String> data = (Map) dataSnapshot.getValue();
                if(data != null) {
                    if (!(String.valueOf(data.get("RequestTo")).equals("null") || data.get("RequestTo") == null)) {
                        FirebaseDatabase.getInstance().getReference().child("Student").child(firebaseUserHeader).child("RequestTo").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.i("Running1", "I am running");
                                teacherRequested.clear();
                                for (final DataSnapshot d : dataSnapshot.getChildren()) {
                                    teacherRequested.add(d.getKey());
                                    if (teacherRequested.size() == dataSnapshot.getChildrenCount()) {
                                        for (int i = 0; i < teacherRequested.size(); i++) {
                                            FirebaseDatabase.getInstance().getReference().child("Student").child(firebaseUserHeader).child("RequestTo").child(teacherRequested.get(i)).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    Log.i("Running2", "I am running");
                                                    Map<String, String> data = (Map) dataSnapshot.getValue();
                                                    if (data != null) {
                                                        if (data.get("Status") != null && !data.get("Status").equals(null)) {
                                                            statusArrayList.add(data.get("Status"));
                                                            if (statusArrayList.size() == teacherRequested.size()) {
                                                                Log.i("CompleteDataSet", statusArrayList.toString() + " " + teacherRequested);
                                                                StudentRequestRecyclerAdapter studentRequestRecyclerAdapter = new StudentRequestRecyclerAdapter(getContext(), teacherRequested, statusArrayList);
                                                                requestsRecyclerView.setHasFixedSize(false);
                                                                requestsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                                                insertScheduleData(statusArrayList, teacherRequested, studentRequestRecyclerAdapter);
                                                                requestsRecyclerView.setAdapter(studentRequestRecyclerAdapter);
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

    public void insertScheduleData(final ArrayList statusArrayList, final ArrayList<String> teacherScheduleArrayList, final StudentRequestRecyclerAdapter requestsRecyclerAdapter){
        for(int i = 0; i < statusArrayList.size(); i++){
            //akash, white
            //accept, accept
            final int j = i;
            if(statusArrayList.get(i).equals("Accept")){
                FirebaseDatabase.getInstance().getReference().child("Teacher").child(teacherScheduleArrayList.get(i)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, String> data = (Map) dataSnapshot.getValue();
                        if(data != null) {
                            if (data.get("Date") != null && data.get("FromTime") != null && data.get("ToTime") != null && data.get("Location") != null) {
                                TeacherTimeManager teacherTimeManager = new TeacherTimeManager(data.get("Date"), data.get("FromTime"), data.get("ToTime"), data.get("Location"));
                                if (teacherScheduleArrayList.size() != 0) {
                                    Log.i("TeacherScheduleArrayList",teacherScheduleArrayList.toString());
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Student").child(firebaseUserHeader).child("Upcoming").child(teacherScheduleArrayList.get(j)).child("When");
                                    databaseReference.child("Date").setValue(teacherTimeManager.getDate());
                                    databaseReference.child("FromTime").setValue(teacherTimeManager.getFromTime());
                                    databaseReference.child("ToTime").setValue(teacherTimeManager.getToTime());
                                    databaseReference.child("Location").setValue(teacherTimeManager.getLocation());
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
    }

    public void showScheduleData() {
        FirebaseDatabase.getInstance().getReference().child("Student").child(firebaseUserHeader).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                teacherTimeManagerArrayList.clear();
                teacherScheduleArrayList.clear();
                final Map<String, String> data = (Map) dataSnapshot.getValue();
                Log.i("Data", data.toString());
                if (!(String.valueOf(data.get("Upcoming")).equals("null") || (String.valueOf(data.get("Upcoming")) == null))) {
                    FirebaseDatabase.getInstance().getReference().child("Student").child(firebaseUserHeader).child("Upcoming").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                teacherScheduleArrayList.add(d.getKey());
                                Log.i("Value", teacherScheduleArrayList.toString());
                                if (teacherScheduleArrayList.size() == dataSnapshot.getChildrenCount()) {
                                    for (int i = 0; i < teacherScheduleArrayList.size(); i++) {
                                        FirebaseDatabase.getInstance().getReference().child("Student").child(firebaseUserHeader).child("Upcoming").child(teacherScheduleArrayList.get(i)).child("When").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.getValue() != null) {
                                                    Map<String, String> data = (Map) dataSnapshot.getValue();
                                                    if (data.get("Date") != null && data.get("FromTime") != null && data.get("ToTime") != null && data.get("ToTime") != null) {
                                                        TeacherTimeManager teacherTimeManager = new TeacherTimeManager(data.get("Date"), data.get("FromTime"), data.get("ToTime"), data.get("Location"));
                                                        teacherTimeManagerArrayList.add(teacherTimeManager);
                                                        if (teacherTimeManagerArrayList.size() == teacherScheduleArrayList.size()) {
                                                            scheduleRecyclerAdapter = new ScheduleRecyclerAdapter(getActivity(), teacherTimeManagerArrayList, teacherScheduleArrayList, false);
                                                            upcomingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                                            upcomingRecyclerView.setHasFixedSize(false);
                                                            upcomingRecyclerView.setAdapter(scheduleRecyclerAdapter);
                                                        }
                                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                                        for (int i = 0; i < teacherTimeManagerArrayList.size(); i++) {
                                                            Log.i("TeacherTimeManager",String.valueOf(teacherTimeManagerArrayList.size()));
                                                            try {
                                                                if (!(simpleDateFormat.parse(teacherTimeManagerArrayList.get(i).getDate()).after(simpleDateFormat.parse(simpleDateFormat.format(new Date()))))) {
                                                                    if (StudentDashboard.hasSubmit) {
                                                                        FirebaseDatabase.getInstance().getReference().child("Student").child(firebaseUserHeader).child("RequestTo").child(teacherScheduleArrayList.get(i)).removeValue();
                                                                        FirebaseDatabase.getInstance().getReference().child("Student").child(firebaseUserHeader).child("Upcoming").child(teacherScheduleArrayList.get(i)).removeValue();
                                                                    }
                                                                }
                                                            } catch (ParseException e) {
                                                                e.printStackTrace();
                                                            }
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
                            }
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

    public void showRatingRecyclerView(){
        FirebaseDatabase.getInstance().getReference().child("Student").child(firebaseUserHeader).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Map data = (Map) dataSnapshot.getValue();
                if(!((data.get("UsersToRate") == null) || data.get("UsersToRate").equals("null"))){
                    FirebaseDatabase.getInstance().getReference().child("Student").child(firebaseUserHeader).child("UsersToRate").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<String> usersToRate = new ArrayList<>();
                            for(DataSnapshot d: dataSnapshot.getChildren()){
                                usersToRate.add(d.getKey());
                            }
                            RatingRecyclerAdapter ratingRecyclerAdapter = new RatingRecyclerAdapter(getActivity(), usersToRate, true);
                            ratingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            ratingRecyclerView.setHasFixedSize(false);
                            ratingRecyclerView.setAdapter(ratingRecyclerAdapter);
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

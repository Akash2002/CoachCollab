package com.example.akash.coachcollab;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;


public class DashboardFragment extends Fragment {

    private RecyclerView notificationDashboardRecyclerView;
    private RecyclerView scheduleRecyclerView;
    private ArrayList<String> scheduleNameArrayList;
    private ArrayList<TeacherTimeManager> teacherTimeManagerArrayList;
    private RecyclerView ratingRecyclerView;
    public static String firebaseUserHeader;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_dashboard, container, false);
        MainActivity.toolbar.setTitle("Dashboard");
        setHasOptionsMenu(true);

        final String currentUserFirebaseHeader = FirebaseAuth.getInstance().getCurrentUser().getEmail().substring(0, FirebaseAuth.getInstance().getCurrentUser().getEmail().indexOf("@"));
        final ArrayList<String> requestNameArrayList = new ArrayList();
        notificationDashboardRecyclerView = v.findViewById(R.id.notificationDashboardRequestRecyclerView);
        scheduleRecyclerView = v.findViewById(R.id.scheduleRecyclerView);
        scheduleNameArrayList = new ArrayList<>();
        teacherTimeManagerArrayList = new ArrayList<>();
        ratingRecyclerView = v.findViewById(R.id.ratingRecyclerView);
        firebaseUserHeader = FirebaseAuth.getInstance().getCurrentUser().getEmail().substring(0, FirebaseAuth.getInstance().getCurrentUser().getEmail().indexOf("@"));
        FirebaseDatabase.getInstance().getReference().child("Teacher").child(currentUserFirebaseHeader).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> data = (Map) dataSnapshot.getValue();
                if(data != null) {
                    if (!(String.valueOf(data.get("Requests")).equals("null") || String.valueOf(data.get("Requests")) == null)) {
                        FirebaseDatabase.getInstance().getReference().child("Teacher").child(currentUserFirebaseHeader).child("Requests").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot d : dataSnapshot.getChildren()) {
                                    requestNameArrayList.add(d.getKey());
                                    if (requestNameArrayList.size() == dataSnapshot.getChildrenCount()) {
                                        RequestsRecyclerAdapter requestsRecyclerAdapter = new RequestsRecyclerAdapter(getActivity(), requestNameArrayList);
                                        notificationDashboardRecyclerView.setHasFixedSize(false);
                                        notificationDashboardRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                        notificationDashboardRecyclerView.setAdapter(requestsRecyclerAdapter);
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

        FirebaseDatabase.getInstance().getReference().child("Teacher").child(currentUserFirebaseHeader).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> data = (Map) dataSnapshot.getValue();
                if(data != null) {
                    if (!(String.valueOf(data.get("Schedule")).equals("null") || String.valueOf(data.get("Schedule")) == null)) {
                        FirebaseDatabase.getInstance().getReference().child("Teacher").child(currentUserFirebaseHeader).child("Schedule").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot d : dataSnapshot.getChildren()) {
                                    scheduleNameArrayList.add(d.getKey());
                                    if (scheduleNameArrayList.size() == dataSnapshot.getChildrenCount()) {
                                        for (int i = 0; i < scheduleNameArrayList.size(); i++) {
                                            FirebaseDatabase.getInstance().getReference().child("Teacher").child(currentUserFirebaseHeader).child("Schedule").child(scheduleNameArrayList.get(i)).child("When").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    Map<String, String> data = (Map) dataSnapshot.getValue();
                                                    if (data != null) {
                                                        if (data.get("Date") != null && data.get("ToTime") != null && data.get("FromTime") != null && data.get("Location") != null) {
                                                            TeacherTimeManager teacherTimeManager = new TeacherTimeManager(data.get("Date"), data.get("ToTime"), data.get("FromTime"), data.get("Location"));
                                                            teacherTimeManagerArrayList.add(teacherTimeManager);
                                                            ScheduleRecyclerAdapter scheduleRecyclerAdapter = new ScheduleRecyclerAdapter(getActivity(), teacherTimeManagerArrayList, scheduleNameArrayList, true);
                                                            scheduleRecyclerView.setHasFixedSize(false);
                                                            scheduleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                                            scheduleRecyclerView.setAdapter(scheduleRecyclerAdapter);
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
        rateUserCardView();
        return v;
    }

    public void rateUserCardView(){
        final String firebaseUserHeader = FirebaseAuth.getInstance().getCurrentUser().getEmail().substring(0, FirebaseAuth.getInstance().getCurrentUser().getEmail().indexOf("@"));
        FirebaseDatabase.getInstance().getReference().child("Teacher").child(firebaseUserHeader).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Map data = (Map) dataSnapshot.getValue();
                if(!((data.get("UsersToRate") == null) || data.get("UsersToRate").equals("null"))){
                    FirebaseDatabase.getInstance().getReference().child("Teacher").child(firebaseUserHeader).child("UsersToRate").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<String> usersToRate = new ArrayList<>();
                            for(DataSnapshot d: dataSnapshot.getChildren()){
                                usersToRate.add(d.getKey());
                            }
                            RatingRecyclerAdapter ratingRecyclerAdapter = new RatingRecyclerAdapter(getActivity(), usersToRate, false);
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


package com.example.akash.coachcollab;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class MyStudentsFragment extends Fragment {

    private RecyclerView allstudentsrecyclerview;
    private String FIREBASE_HEADER = FirebaseAuth.getInstance().getCurrentUser().getEmail().substring(0,FirebaseAuth.getInstance().getCurrentUser().getEmail().indexOf("@"));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_students, container, false);

        allstudentsrecyclerview = v.findViewById(R.id.allstudentsrecyclerview);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Teacher").child(FIREBASE_HEADER).child("MyStudents");
        final ArrayList<UserRecyclerItem> userRecyclerItemArrayList = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<String> studentNames = new ArrayList<>();
                for(DataSnapshot d: dataSnapshot.getChildren()){
                    studentNames.add(d.getKey());
                    if(studentNames.size() == dataSnapshot.getChildrenCount()){
                        for(int i = 0; i < studentNames.size(); i++){
                            databaseReference.child(studentNames.get(i)).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Map<String, String> data = (Map) dataSnapshot.getValue();
                                    Log.i("Data", data.toString());
                                    UserRecyclerItem userRecycler = new UserRecyclerItem(data.get("Name"), data.get("Subjects"), data.get("Grade Level"));
                                    userRecyclerItemArrayList.add(userRecycler);
                                    AllUsersRecyclerAdapter allUsersRecyclerAdapter = new AllUsersRecyclerAdapter(userRecyclerItemArrayList, true);
                                    allstudentsrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    allstudentsrecyclerview.setHasFixedSize(false);
                                    allstudentsrecyclerview.setAdapter(allUsersRecyclerAdapter);
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

}

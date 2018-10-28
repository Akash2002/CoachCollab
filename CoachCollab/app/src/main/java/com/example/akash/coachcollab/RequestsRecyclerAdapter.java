package com.example.akash.coachcollab;

import android.app.DownloadManager;
import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by akash on 12/31/2017.
 */

public class RequestsRecyclerAdapter extends RecyclerView.Adapter<RequestsRecyclerAdapter.ViewHolder>{

    private Context context;
    private ArrayList<String> requestFromNames;
    String currentUserFirebaseHeader = FirebaseAuth.getInstance().getCurrentUser().getEmail().substring(0, FirebaseAuth.getInstance().getCurrentUser().getEmail().indexOf("@"));

    public RequestsRecyclerAdapter(Context context, ArrayList<String> requestFromData){
        this.context = context;
        this.requestFromNames = requestFromData;
    }

    @Override
    public RequestsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_dashboard_notification_recycler_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RequestsRecyclerAdapter.ViewHolder holder, int position) {
        String name = requestFromNames.get(position);
        holder.requestFromNameTextView.setText(name);
    }

    @Override
    public int getItemCount() {
        return requestFromNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView requestFromNameTextView;
        private Button accept, reject;
        int counter = 0;

        public ViewHolder(View itemView) {
            super(itemView);
            requestFromNameTextView = itemView.findViewById(R.id.requestFromName);
            accept = itemView.findViewById(R.id.acceptRequestButton);
            reject = itemView.findViewById(R.id.rejectRequestButton);

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase.getInstance().getReference().child("Student").child(requestFromNameTextView.getText().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Map<String, String> data = (Map) dataSnapshot.getValue();
                            String name = data.get("Name");
                            String subjects = String.valueOf(data.get("Subjects"));
                            String grade = String.valueOf(data.get("Grade Level"));
                            FirebaseDatabase.getInstance().getReference().child("Teacher").child(currentUserFirebaseHeader).child("MyStudents").child(requestFromNameTextView.getText().toString()).child("Name").setValue(name);
                            FirebaseDatabase.getInstance().getReference().child("Teacher").child(currentUserFirebaseHeader).child("MyStudents").child(requestFromNameTextView.getText().toString()).child("Subjects").setValue(subjects);
                            FirebaseDatabase.getInstance().getReference().child("Teacher").child(currentUserFirebaseHeader).child("MyStudents").child(requestFromNameTextView.getText().toString()).child("Grade Level").setValue(grade);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    FirebaseDatabase.getInstance().getReference().child("Student").child(requestFromNameTextView.getText().toString()).child("RequestTo").child(currentUserFirebaseHeader).child("Status").setValue("Accept");
                    FirebaseDatabase.getInstance().getReference().child("Teacher").child(currentUserFirebaseHeader).child("UsersToRate").child(requestFromNameTextView.getText().toString()).setValue(0);
                    FirebaseDatabase.getInstance().getReference().child("Teacher").child(currentUserFirebaseHeader).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Map<String,String> data = (Map) dataSnapshot.getValue();
                            TeacherTimeManager teacherTimeManager = new TeacherTimeManager(data.get("Date"), data.get("ToTime"), data.get("FromTime"), data.get("Location"));
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Teacher").child(currentUserFirebaseHeader).child("Schedule").child(requestFromNameTextView.getText().toString()).child("When");
                            databaseReference.child("Date").setValue(teacherTimeManager.getDate());
                            databaseReference.child("FromTime").setValue(teacherTimeManager.getFromTime());
                            databaseReference.child("ToTime").setValue(teacherTimeManager.getToTime());
                            databaseReference.child("Location").setValue(teacherTimeManager.getLocation());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    FirebaseDatabase.getInstance().getReference().child("Student").child(requestFromNameTextView.getText().toString()).child("UsersToRate").child(currentUserFirebaseHeader).setValue(0);
                    FirebaseDatabase.getInstance().getReference().child("Teacher").child(currentUserFirebaseHeader).child("Requests").child(requestFromNameTextView.getText().toString()).removeValue();
                }
            });

            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase.getInstance().getReference().child("Student").child(requestFromNameTextView.getText().toString()).child("RequestTo").child(currentUserFirebaseHeader).child("Status").setValue("Reject");
                    FirebaseDatabase.getInstance().getReference().child("Teacher").child(currentUserFirebaseHeader).child("Requests").child(requestFromNameTextView.getText().toString()).removeValue();
                }
            });
        }
    }

}

package com.example.akash.coachcollab;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by akash on 1/1/2018.
 */

public class ScheduleRecyclerAdapter extends RecyclerView.Adapter<ScheduleRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<TeacherTimeManager> teacherTimeManagerArrayList;
    private ArrayList<String> studentNames;
    private boolean isStudent;

    public ScheduleRecyclerAdapter(Context context, ArrayList<TeacherTimeManager> teacherTimeManagerArrayList, ArrayList<String> studentNames, boolean isStudent) {
        this.context = context;
        this.teacherTimeManagerArrayList = teacherTimeManagerArrayList;
        this.studentNames = studentNames;
        this.isStudent = isStudent;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_recycler_view_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TeacherTimeManager teacherTimeManager = teacherTimeManagerArrayList.get(position);
        String studentName = studentNames.get(position);
        holder.nameTextView.setText(studentName);
        holder.timeTextView.setText(teacherTimeManager.getFromTime() + " - " + teacherTimeManager.getToTime());
        holder.locationTextView.setText(teacherTimeManager.getLocation());
        holder.dateTextView.setText(teacherTimeManager.getDate());
    }

    @Override
    public int getItemCount() {
        return teacherTimeManagerArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nameTextView, dateTextView, locationTextView, timeTextView;

        public ViewHolder(final View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isStudent){
                        FirebaseDatabase.getInstance().getReference().child("Teacher").child(nameTextView.getText().toString()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Map<String, String> data = (Map) dataSnapshot.getValue();
                                Intent intent = new Intent(itemView.getContext(), TeacherDetailsActivity.class);
                                intent.putExtra("Description", data.get("Description"));
                                intent.putExtra("Email", data.get("Email"));
                                intent.putExtra("Grade Level", String.valueOf(data.get("GradeLevel")));
                                intent.putExtra("Name", data.get("Name"));
                                intent.putExtra("Rating", String.valueOf(data.get("Rating")));
                                intent.putExtra("Subjects", String.valueOf(data.get("Subjects")));
                                intent.putExtra("UserType", data.get("Type"));
                                intent.putExtra("Availability", data.get("Availability"));
                                intent.putExtra("Date",data.get("Date"));
                                intent.putExtra("FromTime",data.get("FromTime"));
                                intent.putExtra("ToTime", data.get("ToTime"));
                                intent.putExtra("Location",data.get("Location"));
                                itemView.getContext().startActivity(intent);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } else {
                        FirebaseDatabase.getInstance().getReference().child("Student").child(nameTextView.getText().toString()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Map<String, String> data = (Map) dataSnapshot.getValue();
                                Intent intent = new Intent(itemView.getContext(), StudentDetailsActivity.class);
                                intent.putExtra("Description", data.get("Description"));
                                intent.putExtra("Email", data.get("Email"));
                                intent.putExtra("Grade Level", String.valueOf(data.get("GradeLevel")));
                                intent.putExtra("Name", data.get("Name"));
                                intent.putExtra("Rating", String.valueOf(data.get("Rating")));
                                intent.putExtra("Subjects", String.valueOf(data.get("Subjects")));
                                intent.putExtra("UserType", data.get("Type"));
                                itemView.getContext().startActivity(intent);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            });

        }
    }
}

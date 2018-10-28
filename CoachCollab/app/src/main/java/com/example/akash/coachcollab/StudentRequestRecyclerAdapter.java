package com.example.akash.coachcollab;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by akash on 1/1/2018.
 */

public class StudentRequestRecyclerAdapter extends RecyclerView.Adapter<StudentRequestRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> teacherNameArrayList;
    private ArrayList<String> statusArrayList;
    private boolean hasSetText = false;

    public StudentRequestRecyclerAdapter(Context context, ArrayList<String> teacherNameArrayList, ArrayList<String> statusArrayList){
        this.context = context;
        this.teacherNameArrayList = teacherNameArrayList;
        this.statusArrayList = statusArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.student_request_recycler_view_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //akash, white, veerappan
        //accept, reject, reject
        Log.i("ArrayList", statusArrayList + " " + teacherNameArrayList);
        String teacherName = teacherNameArrayList.get(position);
        String status = statusArrayList.get(position);
        holder.teacherNameTextView.setText(teacherName);
        holder.statusTextView.setText(status);
        hasSetText = true;
    }

    @Override
    public int getItemCount() {
        return teacherNameArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView teacherNameTextView;
        TextView statusTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            teacherNameTextView = itemView.findViewById(R.id.teacherRequestedTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
        }
    }
}

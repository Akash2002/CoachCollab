package com.example.akash.coachcollab;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by akash on 12/27/2017.
 */

public class StudentRecyclerAdapter extends RecyclerView.Adapter<StudentRecyclerAdapter.ViewHolder> {

    ArrayList<RecyclerItem> recyclerItemArrayList = new ArrayList<>();
    private Context context;

    public StudentRecyclerAdapter(ArrayList e, Context context){
        recyclerItemArrayList = e;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecyclerItem item = recyclerItemArrayList.get(position);
        holder.heading.setText(item.getHeading());
        holder.description.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return recyclerItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView heading, description;
        public ViewHolder(View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.heading);
            description = itemView.findViewById(R.id.description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(this.heading.getText().equals("Subjects")){
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Student").child(StudentProfile.studentFirebaseHeader).child("Subjects");
                final View v = LayoutInflater.from(context).inflate(R.layout.subjects_alert_list_layout, null);
                final CheckBox mathCheckBox = v.findViewById(R.id.mathCheckBox);
                final CheckBox scienceCheckBox = v.findViewById(R.id.scienceCheckBox);
                final CheckBox socialCheckBox = v.findViewById(R.id.socialCheckBox);
                final CheckBox englishCheckBox = v.findViewById(R.id.englishCheckBox);
                final CheckBox computerCheckBox = v.findViewById(R.id.computerCheckBox);
                Button chooseButton = v.findViewById(R.id.chooseButton);
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setView(v);
                chooseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<String> subjects = new ArrayList<>();
                        databaseReference.setValue(subjects);
                        if(mathCheckBox.isChecked() == true){
                            subjects.add(mathCheckBox.getText().toString());
                        }
                        if(scienceCheckBox.isChecked() == true){
                            subjects.add(scienceCheckBox.getText().toString());
                        }
                        if(socialCheckBox.isChecked() == true){
                            subjects.add(socialCheckBox.getText().toString());
                        }
                        if(englishCheckBox.isChecked() == true){
                            subjects.add(englishCheckBox.getText().toString());
                        }
                        if(computerCheckBox.isChecked() == true){
                            subjects.add(computerCheckBox.getText().toString());
                        }
                        for(int j = 0; j < subjects.size(); j++){
                            databaseReference.child(String.valueOf(j)).setValue(subjects.get(j));
                        }
                    }
                });
                alert.create().show();
            } else if (this.heading.getText().equals("Grade Level")){
                final View v = LayoutInflater.from(context).inflate(R.layout.grade_alert_layout, null);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                Button chooseButton = v.findViewById(R.id.chooseButton);
                        alertDialog.setView(v);
                chooseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RadioGroup radioGroup = v.findViewById(R.id.radioGroup);
                        RadioButton gradeLevel = v.findViewById(radioGroup.getCheckedRadioButtonId());
                        FirebaseDatabase.getInstance().getReference().child("Student").child(StudentProfile.studentFirebaseHeader).child("Grade Level").setValue(gradeLevel.getText().toString());
                    }
                });
                alertDialog.create().show();
            }
        }
    }

}

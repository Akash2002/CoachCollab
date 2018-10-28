package com.example.akash.coachcollab;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by akash on 12/23/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private List<RecyclerItem> recyclerItemsList;
    private Context context;
    private FragmentManager fragmentManager;
    public static boolean stringFormat = false;
    public static boolean formatGrade = false;
    private boolean fromProfile;
    private boolean isStudent;

    public RecyclerViewAdapter(List<RecyclerItem> recyclerItemsList, Context context, boolean fromProfile, boolean isStudent) {
        this.recyclerItemsList = recyclerItemsList;
        this.context = context;
        this.fromProfile = fromProfile;
        this.isStudent = isStudent;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecyclerItem recyclerItem = recyclerItemsList.get(position);
        holder.heading.setText(recyclerItem.getHeading());
        holder.description.setText(recyclerItem.getDescription());
    }

    @Override
    public int getItemCount() {
        return recyclerItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView heading;
        public TextView description;

        public ViewHolder(View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.heading);
            description = itemView.findViewById(R.id.description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (fromProfile) {
                Log.i("Test", "Click");
                TimePickerFragment timePicker = new TimePickerFragment();
                DatePickerFragmentDialog datePickerFragmentDialog = new DatePickerFragmentDialog();
                if (this.heading.getText().equals("From Time")) {
                    timePicker.setTimeId(0);
                    timePicker.show(getFragmentManager(), "timepicker");
                } else if (this.heading.getText().equals("To Time")) {
                    timePicker.setTimeId(1);
                    timePicker.show(fragmentManager, "timepicker");
                } else if (this.heading.getText().equals("Date")) {
                    datePickerFragmentDialog.show(fragmentManager, "datepicker");
                } else if (this.heading.getText().equals("Location")) {
                    final View v = LayoutInflater.from(context).inflate(R.layout.place_alert_dialog_layout, null);
                    final Button setButton = v.findViewById(R.id.setButton);
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                            .setView(v);
                    setButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditText locationEditText = v.findViewById(R.id.locationEditText);
                            String location = locationEditText.getText().toString();
                            FirebaseDatabase.getInstance().getReference().child("Teacher").child(MainActivity.firebaseUserHeader).child("Location").setValue(location);
                            AlertDialog alert = alertDialog.create();
                            alert.dismiss();
                        }
                    });
                    alertDialog.create().show();
                } else if (this.heading.getText().equals("Subjects")) {
                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Teacher").child(MainActivity.firebaseUserHeader).child("Subjects");
                    final View v = LayoutInflater.from(context).inflate(R.layout.subjects_alert_list_layout, null);
                    final CheckBox mathCheckBox = v.findViewById(R.id.mathCheckBox);
                    final CheckBox scienceCheckBox = v.findViewById(R.id.scienceCheckBox);
                    final CheckBox socialCheckBox = v.findViewById(R.id.socialCheckBox);
                    final CheckBox englishCheckBox = v.findViewById(R.id.englishCheckBox);
                    final CheckBox computerCheckBox = v.findViewById(R.id.computerCheckBox);
                    final Button chooseButton = v.findViewById(R.id.chooseButton);
                    final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setView(v);
                    chooseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ArrayList<String> subjects = new ArrayList<>();
                            databaseReference.setValue(subjects);
                            stringFormat = false;
                            if (mathCheckBox.isChecked() == true) {
                                subjects.add(mathCheckBox.getText().toString());
                            }
                            if (scienceCheckBox.isChecked() == true) {
                                subjects.add(scienceCheckBox.getText().toString());
                            }
                            if (socialCheckBox.isChecked() == true) {
                                subjects.add(socialCheckBox.getText().toString());
                            }
                            if (englishCheckBox.isChecked() == true) {
                                subjects.add(englishCheckBox.getText().toString());
                            }
                            if (computerCheckBox.isChecked() == true) {
                                subjects.add(computerCheckBox.getText().toString());
                            }
                            for (int j = 0; j < subjects.size(); j++) {
                                databaseReference.child(String.valueOf(j)).setValue(subjects.get(j));
                            }
                            stringFormat = true;
                            alert.create().dismiss();
                        }
                    });
                    alert.create().show();
                } else if (this.heading.getText().equals("Grade Level")) {
                    final View v = LayoutInflater.from(context).inflate(R.layout.grade_level_alert_layout, null);
                    final CheckBox elementaryCheckBox = v.findViewById(R.id.elementaryCheckBox);
                    final CheckBox middleCheckBox = v.findViewById(R.id.middleCheckBox);
                    final CheckBox highCheckBox = v.findViewById(R.id.highCheckBox);
                    Button chooseButton = v.findViewById(R.id.chooseButton);
                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Teacher").child(MainActivity.firebaseUserHeader).child("GradeLevel");
                    final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setView(v);
                    chooseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ArrayList<String> teachingLevels = new ArrayList<>();
                            databaseReference.setValue(teachingLevels);
                            formatGrade = false;
                            if (elementaryCheckBox.isChecked()) {
                                teachingLevels.add(elementaryCheckBox.getText().toString());
                            }
                            if (middleCheckBox.isChecked()) {
                                teachingLevels.add(middleCheckBox.getText().toString());
                            }
                            if (highCheckBox.isChecked()) {
                                teachingLevels.add(highCheckBox.getText().toString());
                            }
                            for (int j = 0; j < teachingLevels.size(); j++) {
                                databaseReference.child(String.valueOf(j)).setValue(teachingLevels.get(j));
                            }
                            formatGrade = true;
                            alert.create().dismiss();
                        }
                    });
                    alert.create().show();
                }
                notifyDataSetChanged();
            } else if (!fromProfile) {
                if (heading.getText().equals("Description")) {
                    if (isStudent) {
                        final View v = LayoutInflater.from(context).inflate(R.layout.description_alert_layout, null);
                        TextView textView = v.findViewById(R.id.descriptionText);
                        textView.setText(TeacherDetailsActivity.description);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setView(v);
                        builder.create().show();
                    } else {
                        final View v = LayoutInflater.from(context).inflate(R.layout.description_alert_layout, null);
                        TextView textView = v.findViewById(R.id.descriptionText);
                        textView.setText(StudentDetailsActivity.description);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setView(v);
                        builder.create().show();
                    }
                }
            }
        }
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        System.out.println(this.fragmentManager + "Fragment Manager");
    }
}

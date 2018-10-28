package com.example.akash.coachcollab;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

/**
 * Created by akash on 12/24/2017.
 */

public class TimePickerFragment extends DialogFragment implements android.app.TimePickerDialog.OnTimeSetListener {

    private int hour;
    private String minute;
    private DatabaseReference dbRef;
    private int TIME_ID;

    public int getTimeId() {
        return TIME_ID;
    }

    public void setTimeId(int timeId) {
        TIME_ID = timeId;
    }

    public TimePickerFragment(){
        dbRef = FirebaseDatabase.getInstance().getReference().child("Teacher");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c =  Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new android.app.TimePickerDialog(getActivity(), this, hour, minute, false);
    }

    @Override
    public void onTimeSet(android.widget.TimePicker timePicker, int hour, int minute) {
        //00 CHECK 0-10 CHECK
        this.hour = hour;
        this.minute = String.valueOf(minute);
        String AM_PM = "";
        if(hour < 12){
            AM_PM = "AM";
        } else if(hour > 12){
            this.hour = hour - 12;
            AM_PM = "PM";
        }
        if(String.valueOf(minute).length() < 2){
            this.minute = "0" + minute;
        }
        if(this.getTimeId() == 0) {
            dbRef.child(ProfileFragment.userDisplayName).child("FromTime").setValue(this.hour + ":" + this.minute + " " + AM_PM);
        } else if(this.getTimeId() == 1){
            dbRef.child(ProfileFragment.userDisplayName).child("ToTime").setValue(this.hour + ":" + this.minute + " " + AM_PM);
        }
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }
}

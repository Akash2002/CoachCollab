package com.example.akash.coachcollab;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.DatePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

/**
 * Created by akash on 12/25/2017.
 */

public class DatePickerFragmentDialog extends DialogFragment {

    private Context context;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                FirebaseDatabase.getInstance().getReference().child("Teacher").child(MainActivity.firebaseUserHeader).child("Date").setValue(month + "/" + date + "/" + year);
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        return datePickerDialog;
    }

    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

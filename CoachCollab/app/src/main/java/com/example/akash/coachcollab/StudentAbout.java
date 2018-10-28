package com.example.akash.coachcollab;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StudentAbout extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        StudentMainActivity.toolbar.setTitle("About");
        return inflater.inflate(R.layout.fragment_student_about, container, false);
    }

}

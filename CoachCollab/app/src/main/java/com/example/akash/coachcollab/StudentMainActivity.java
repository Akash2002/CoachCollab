package com.example.akash.coachcollab;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentMainActivity extends AppCompatActivity{
    public static Toolbar toolbar;
    Fragment goToFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        goToFragment = new StudentDashboard();
        getFragmentManager().beginTransaction().replace(R.id.container1, goToFragment).commit();

        if(!(getIntent() == null)){
            System.out.println("Intent Reload Test " + getIntent());
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.student_dashboard:
                        goToFragment = new StudentDashboard();
                        break;
                    case R.id.students_about:
                        goToFragment = new StudentAbout();
                        break;
                    case R.id.student_profile:
                        goToFragment = new StudentProfile();
                        break;
                    case R.id.student_users:
                        goToFragment = new StudentAllUsers();
                        break;
                }
                getFragmentManager().beginTransaction().replace(R.id.container1, goToFragment).commit();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }

        });

        if(getIntent().getStringExtra("FromWhere").equals("CreateAccount")){
            databaseReference = databaseReference.child("Student").child(FirebaseAuth.getInstance().getCurrentUser().getEmail().substring(0,FirebaseAuth.getInstance().getCurrentUser().getEmail().indexOf("@")));
            databaseReference.child("Name").setValue(getIntent().getStringExtra("Name"));
            databaseReference.child("Type").setValue(getIntent().getStringExtra("UserType"));
            databaseReference.child("Email").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            databaseReference.child("Description").setValue("Description");
            databaseReference.child("Rating").setValue("0");
            databaseReference.child("GradeLevel").setValue("None");
            databaseReference.child("Subjects").setValue("None");
            databaseReference.child("NumRatings").setValue("0");
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

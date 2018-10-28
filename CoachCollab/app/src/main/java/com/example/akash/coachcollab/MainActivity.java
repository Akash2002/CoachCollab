package com.example.akash.coachcollab;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity{

    private DrawerLayout drawer;
    private Fragment goToFragment;
    public static String firebaseUserHeader;
    private DatabaseReference databaseReference;
    public static Context context = null;
    public static Toolbar toolbar;
    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Teacher");
        firebaseUserHeader = FirebaseAuth.getInstance().getCurrentUser().getEmail().substring(0,FirebaseAuth.getInstance().getCurrentUser().getEmail().indexOf("@"));;

        context = getApplicationContext();

        if(getIntent().getStringExtra("FromWhere").equals("CreateAccount")){
            databaseReference = databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getEmail().substring(0,FirebaseAuth.getInstance().getCurrentUser().getEmail().indexOf("@")));
            databaseReference.child("Name").setValue(getIntent().getStringExtra("Name"));
            databaseReference.child("Type").setValue(getIntent().getStringExtra("UserType"));
            databaseReference.child("FromTime").setValue(0);
            databaseReference.child("ToTime").setValue(0);
            databaseReference.child("Location").setValue("Location");
            databaseReference.child("Availability").setValue("No");
            databaseReference.child("Email").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            databaseReference.child("Days").setValue("None");
            databaseReference.child("Description").setValue("Description");
            databaseReference.child("Rating").setValue("0");
            databaseReference.child("GradeLevel").setValue("None");
            databaseReference.child("Subjects").setValue("None");
            databaseReference.child("NumRatings").setValue("0");
        }

        goToFragment = new DashboardFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, goToFragment).commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboard_nav:
                        goToFragment = new DashboardFragment();
                        break;
                    case R.id.about_nav:
                        goToFragment = new AboutFragment();
                        break;
                    case R.id.profile_nav:
                        goToFragment = new ProfileFragment();
                        break;
                    case R.id.student_nav:
                        goToFragment = new UsersFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.container, goToFragment).commit();

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
        }
    }


    public DrawerLayout getDrawer() {
        return drawer;
    }

    public void setDrawer(DrawerLayout drawer) {
        this.drawer = drawer;
    }

    public Fragment getGoToFragment() {
        return goToFragment;
    }

    public void setGoToFragment(Fragment goToFragment) {
        this.goToFragment = goToFragment;
    }
}

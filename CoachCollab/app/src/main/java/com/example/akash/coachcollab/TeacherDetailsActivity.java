package com.example.akash.coachcollab;

import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class TeacherDetailsActivity extends AppCompatActivity {

    private Button requestButton;
    public static String description;
    private boolean clickRequest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_details);

        findViewById(R.id.profileConstraintLyaout).requestFocus();
        requestButton = findViewById(R.id.requestButton);

        description = getIntent().getStringExtra("Description");
        String email = getIntent().getStringExtra("Email");
        String gradeLevel = getIntent().getStringExtra("Grade Level");
        String name = getIntent().getStringExtra("Name");
        String rating = getIntent().getStringExtra("Rating");
        String subjects = getIntent().getStringExtra("Subjects");
        String date = getIntent().getStringExtra("Date");
        String fromTime = getIntent().getStringExtra("FromTime");
        String toTime = getIntent().getStringExtra("ToTime");
        String location = getIntent().getStringExtra("Location");
        final String teacherfirebaseHeader = email.substring(0,email.indexOf("@"));

        TextView fullNameTextView = findViewById(R.id.fullNameTextView);
        TextView emailTextView = findViewById(R.id.emailTextView);

        fullNameTextView.setText(name);
        emailTextView.setText(email);

        RecyclerItem descriptionRecyclerItem = new RecyclerItem("Description",format(description));
        RecyclerItem gradeLevelRecyclerItem = new RecyclerItem("Grade Level",gradeLevel);
        RecyclerItem ratingRecyclerItem = new RecyclerItem("Rating",rating);
        RecyclerItem subjectsRecyclerItem = new RecyclerItem("Subject(s)",subjects);
        RecyclerItem dateRecyclerItem = new RecyclerItem("Date", date);
        RecyclerItem toTimeRecyclerItem = new RecyclerItem("ToTime",toTime);
        RecyclerItem fromTimeRecyclerItem = new RecyclerItem("FromTime",fromTime);
        RecyclerItem locationRecyclerItem = new RecyclerItem("Location",location);

        ArrayList<RecyclerItem> recyclerItemArrayList = new ArrayList<>();
        recyclerItemArrayList.add(descriptionRecyclerItem);
        recyclerItemArrayList.add(gradeLevelRecyclerItem);
        recyclerItemArrayList.add(subjectsRecyclerItem);
        recyclerItemArrayList.add(fromTimeRecyclerItem);
        recyclerItemArrayList.add(toTimeRecyclerItem);
        recyclerItemArrayList.add(dateRecyclerItem);
        recyclerItemArrayList.add(locationRecyclerItem);
        recyclerItemArrayList.add(ratingRecyclerItem);

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(recyclerItemArrayList, TeacherDetailsActivity.this, false, true);

        RecyclerView allTeachersRecyclerView = findViewById(R.id.teacherDetailRecyclerView);
        allTeachersRecyclerView.setHasFixedSize(false);
        allTeachersRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        allTeachersRecyclerView.setAdapter(recyclerViewAdapter);

        final String currentUserFirebaseHeader = FirebaseAuth.getInstance().getCurrentUser().getEmail().substring(0, FirebaseAuth.getInstance().getCurrentUser().getEmail().indexOf("@"));
        final DatabaseReference databaseReferenceT = FirebaseDatabase.getInstance().getReference().child("Teacher").child(teacherfirebaseHeader).child("Requests").child(currentUserFirebaseHeader);
        final DatabaseReference databaseReferenceS = FirebaseDatabase.getInstance().getReference().child("Student").child(currentUserFirebaseHeader).child("RequestTo").child(teacherfirebaseHeader);

        FirebaseDatabase.getInstance().getReference().child("Student").child(currentUserFirebaseHeader).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> data = (Map) dataSnapshot.getValue();
                if(String.valueOf(data.get("RequestTo")).equals("null") || data.get("RequestTo") == null){
                    clickRequest = true;
                } else if(!(String.valueOf(data.get("RequestTo")).equals("null") || data.get("RequestTo") == null)){
                    FirebaseDatabase.getInstance().getReference().child("Student").child(currentUserFirebaseHeader).child("RequestTo").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot d: dataSnapshot.getChildren()){
                                if(!(d.getKey().equals(teacherfirebaseHeader))){
                                    clickRequest = true;
                                } else {
                                    clickRequest = false;
                                    return;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickRequest){
                    databaseReferenceT.child("RequestDate").setValue(getCurrentDate());
                    databaseReferenceT.child("Status").setValue("None");
                    databaseReferenceS.child("RequestDate").setValue(getCurrentDate());
                    databaseReferenceS.child("Status").setValue("None");
                    Intent intent = new Intent(TeacherDetailsActivity.this, StudentMainActivity.class);
                    intent.putExtra("FromWhere","TeacherDetails");
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "You have requested previously.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        return month + "-" + day + "-" + year;
    }

    private String format(String string){
        if(string.length() < 30){
            return string;
        } else {
            return string.substring(0,31) + "...";
        }
    }
    /*

     */
}

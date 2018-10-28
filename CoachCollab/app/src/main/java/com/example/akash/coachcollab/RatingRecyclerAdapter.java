package com.example.akash.coachcollab;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Dash;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by akash on 1/2/2018.
 */

public class RatingRecyclerAdapter extends RecyclerView.Adapter<RatingRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> userToRateArrayList;
    public static String name = "";
    private boolean rateTeacher;

    public RatingRecyclerAdapter(Context context, ArrayList<String> userToRateArrayList, boolean rateTeacher) {
        this.context = context;
        this.userToRateArrayList = userToRateArrayList;
        this.rateTeacher = rateTeacher;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_bar_recycler_adapter, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String nameOfUserToRate = userToRateArrayList.get(position);
        holder.userToRateTextView.setText(nameOfUserToRate);
    }

    @Override
    public int getItemCount() {
        return userToRateArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView userToRateTextView;
        RatingBar ratingBar;
        Button submitButton;
        private boolean hasIncrementedRatingNum = false;
        private boolean hasSetRating = false;
        private boolean hasRated = false;

        public ViewHolder(View itemView) {
            super(itemView);
            userToRateTextView = itemView.findViewById(R.id.userNameTextViewRating);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            submitButton = itemView.findViewById(R.id.submitButton);
            final ArrayList<String> usersToRate = new ArrayList<>();
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (rateTeacher) {
                        if (!hasRated) {
                            final double userRating = ratingBar.getRating();
                            FirebaseDatabase.getInstance().getReference().child("Teacher").child(userToRateTextView.getText().toString()).child("NumRatings").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    final Integer numOfRatings = Integer.parseInt(dataSnapshot.getValue(String.class));
                                    if (!hasIncrementedRatingNum) {
                                        FirebaseDatabase.getInstance().getReference().child("Teacher").child(userToRateTextView.getText().toString()).child("NumRatings").setValue(String.valueOf(numOfRatings + 1));
                                        hasIncrementedRatingNum = true;
                                    }
                                    FirebaseDatabase.getInstance().getReference().child("Teacher").child(userToRateTextView.getText().toString()).child("Rating").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Double value = Double.parseDouble(dataSnapshot.getValue(String.class));
                                            Double finalRating = ((numOfRatings * value) + userRating) / (numOfRatings + 1);
                                            if (!hasSetRating) {
                                                FirebaseDatabase.getInstance().getReference().child("Teacher").child(userToRateTextView.getText().toString()).child("Rating").setValue(String.valueOf(new DecimalFormat("#.0").format(finalRating)));
                                                hasSetRating = true;
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            FirebaseDatabase.getInstance().getReference().child("Student").child(StudentDashboard.firebaseUserHeader).child("RequestTo").child(userToRateTextView.getText().toString()).removeValue();
                            FirebaseDatabase.getInstance().getReference().child("Student").child(StudentDashboard.firebaseUserHeader).child("Upcoming").child(userToRateTextView.getText().toString()).removeValue();
                            FirebaseDatabase.getInstance().getReference().child("Student").child(StudentDashboard.firebaseUserHeader).child("UsersToRate").child(userToRateTextView.getText().toString()).removeValue();
                            hasRated = true;
                        }
                    } else if (!rateTeacher){
                        if (!hasRated) {
                            final double userRating = ratingBar.getRating();
                            Toast.makeText(context, "Thank you for rating " + userToRateTextView.getText().toString() + ".", Toast.LENGTH_LONG).show();
                            FirebaseDatabase.getInstance().getReference().child("Student").child(userToRateTextView.getText().toString()).child("NumRatings").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    final Integer numOfRatings = Integer.parseInt(dataSnapshot.getValue(String.class));
                                    if (!hasIncrementedRatingNum) {
                                        FirebaseDatabase.getInstance().getReference().child("Student").child(userToRateTextView.getText().toString()).child("NumRatings").setValue(String.valueOf(numOfRatings + 1));
                                        hasIncrementedRatingNum = true;
                                    }
                                    FirebaseDatabase.getInstance().getReference().child("Student").child(userToRateTextView.getText().toString()).child("Rating").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Double value = Double.parseDouble(dataSnapshot.getValue(String.class));
                                            Double finalRating = ((numOfRatings * value) + userRating) / (numOfRatings + 1);
                                            if (!hasSetRating) {
                                                FirebaseDatabase.getInstance().getReference().child("Student").child(userToRateTextView.getText().toString()).child("Rating").setValue(String.valueOf(new DecimalFormat("#.0").format(finalRating)));
                                                hasSetRating = true;
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            FirebaseDatabase.getInstance().getReference().child("Student").child(userToRateTextView.getText().toString()).child("RequestTo").child(userToRateTextView.getText().toString()).removeValue();
                            FirebaseDatabase.getInstance().getReference().child("Teacher").child(DashboardFragment.firebaseUserHeader).child("Schedule").child(userToRateTextView.getText().toString()).removeValue();
                            FirebaseDatabase.getInstance().getReference().child("Teacher").child(DashboardFragment.firebaseUserHeader).child("UsersToRate").child(userToRateTextView.getText().toString()).removeValue();
                            hasRated = true;
                        }
                    }
                }
            });
        }
    }
}

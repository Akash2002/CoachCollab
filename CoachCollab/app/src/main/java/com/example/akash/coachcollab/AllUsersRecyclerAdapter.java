package com.example.akash.coachcollab;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by akash on 12/27/2017.
 */

public class AllUsersRecyclerAdapter extends RecyclerView.Adapter<AllUsersRecyclerAdapter.ViewHolder>{

    private ArrayList<UserRecyclerItem> userRecyclerItemArrayList;

    private ArrayList<StudentUser> studentUserArrayList;
    private ArrayList<TeacherUser> teacherUserArrayList;
    private boolean isStudent;

    public AllUsersRecyclerAdapter(ArrayList<UserRecyclerItem> userRecyclerItemArrayList, boolean isStudent){
        this.userRecyclerItemArrayList = userRecyclerItemArrayList;
        this.isStudent = isStudent;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_users_recycler_view_layout, parent, false);

        Log.i("RecyclerView1","onCreateViewHolder");
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserRecyclerItem userRecyclerItem = userRecyclerItemArrayList.get(position);
        holder.nameHeader.setText(userRecyclerItem.getNameHeader());
        holder.gradeText.setText(userRecyclerItem.getGradeSideText());
        holder.subjectText.setText(userRecyclerItem.getSubjectSideText());
        Log.i("RecyclerView1", "onBindViewHolder");
    }

    @Override
    public int getItemCount() {
        Log.i("RecyclerView1", String.valueOf(userRecyclerItemArrayList.size()));
        return userRecyclerItemArrayList.size();
    }

    public ArrayList<StudentUser> getStudentUserArrayList() {
        return studentUserArrayList;
    }

    public void setStudentUserArrayList(ArrayList<StudentUser> studentUserArrayList) {
        this.studentUserArrayList = studentUserArrayList;
    }

    public ArrayList<TeacherUser> getTeacherUserArrayList() {
        return teacherUserArrayList;
    }

    public void setTeacherUserArrayList(ArrayList<TeacherUser> teacherUserArrayList) {
        this.teacherUserArrayList = teacherUserArrayList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView nameHeader, gradeText, subjectText;

        public ViewHolder(final View itemView) {
            super(itemView);
            nameHeader = itemView.findViewById(R.id.userNameTextHeader);
            gradeText = itemView.findViewById(R.id.gradeLevelSideSubHeading);
            subjectText = itemView.findViewById(R.id.subjectTextSideHeader);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isStudent){
                        Intent intent = new Intent(itemView.getContext(), StudentDetailsActivity.class);
                        if(studentUserArrayList != null){
                            for(int j = 0; j < studentUserArrayList.size(); j++){
                                if(formatText(studentUserArrayList.get(j).getName()).equals(formatText(nameHeader.getText().toString()))){
                                    intent.putExtra("Description", studentUserArrayList.get(j).getDescription());
                                    intent.putExtra("Email", studentUserArrayList.get(j).getEmail());
                                    intent.putExtra("Grade Level", studentUserArrayList.get(j).getGradeLevel());
                                    intent.putExtra("Name", studentUserArrayList.get(j).getName());
                                    intent.putExtra("Rating", studentUserArrayList.get(j).getRating());
                                    intent.putExtra("Subjects", studentUserArrayList.get(j).getSubject());
                                    intent.putExtra("UserType", studentUserArrayList.get(j).getType());
                                    itemView.getContext().startActivity(intent);
                                }
                            }
                        }
                    } else if (!isStudent){
                        Intent intent = new Intent(itemView.getContext(), TeacherDetailsActivity.class);
                        if(teacherUserArrayList!=null){
                            for(int j = 0; j < teacherUserArrayList.size(); j++){
                                if(formatText(teacherUserArrayList.get(j).getUserName()).equals(formatText(nameHeader.getText().toString()))){
                                    intent.putExtra("Description", teacherUserArrayList.get(j).getUserDescription());
                                    intent.putExtra("Email", teacherUserArrayList.get(j).getUserEmail());
                                    intent.putExtra("Grade Level", teacherUserArrayList.get(j).getGradeLevel());
                                    intent.putExtra("Name", teacherUserArrayList.get(j).getUserName());
                                    intent.putExtra("Rating", teacherUserArrayList.get(j).getRating());
                                    intent.putExtra("Subjects", teacherUserArrayList.get(j).getSubjectMap());
                                    intent.putExtra("Availability", teacherUserArrayList.get(j).getAvailability());
                                    intent.putExtra("Date",teacherUserArrayList.get(j).getDate());
                                    intent.putExtra("FromTime",teacherUserArrayList.get(j).getUserFromTime());
                                    intent.putExtra("ToTime",teacherUserArrayList.get(j).getUserToTime());
                                    intent.putExtra("Location",teacherUserArrayList.get(j).getUserLocation());
                                    intent.putExtra("UserType", "Teacher");
                                    intent.putExtra("UserDisplayName",teacherUserArrayList.get(j).getUserDisplayName());
                                    itemView.getContext().startActivity(intent);
                                }
                            }
                        }
                    }
                }
            });

        }
    }

    public String formatText(String string){
        if(string != null) {
            if (string.length() > 15) {
                return string.substring(0, 16) + "...";
            }
        }
        return string;
    }

}

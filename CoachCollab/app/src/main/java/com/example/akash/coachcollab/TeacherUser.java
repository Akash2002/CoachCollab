package com.example.akash.coachcollab;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by akash on 12/24/2017.
 */

public class TeacherUser {
    private String userName;
    private String userEmail;
    private String userFromTime;
    private String userToTime;
    private String userLocation;
    private String userDisplayName;
    private String date;
    private String userDescription;
    private String availability;
    private String rating;
    private String subjectMap;
    private String gradeLevel;

    public TeacherUser(String subjectMap, String gradeLevel, String userName, String userEmail, String userFromTime, String userToTime, String userLocation, String userDisplayName, String date, String userDescription, String availability, String rating) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userFromTime = userFromTime;
        this.userToTime = userToTime;
        this.userLocation = userLocation;
        this.userDisplayName = userDisplayName;
        this.date = date;
        this.userDescription = userDescription;
        this.availability = availability;
        this.rating = rating;
        this.subjectMap = subjectMap;
        this.gradeLevel = gradeLevel;
    }


    public TeacherUser() {
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public String getSubjectMap() {
        return subjectMap;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public void setSubjectMap(String subjectMap) {
        this.subjectMap = subjectMap;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserFromTime() {
        return userFromTime;
    }

    public void setUserFromTime(String userFromTime) {
        this.userFromTime = userFromTime;
    }

    public String getUserToTime() {
        return userToTime;
    }

    public void setUserToTime(String userToTime) {
        this.userToTime = userToTime;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public ArrayList<String> stringToArrayList(String data){
        return new ArrayList<String>(Arrays.asList(data.split(",")));
    }

}

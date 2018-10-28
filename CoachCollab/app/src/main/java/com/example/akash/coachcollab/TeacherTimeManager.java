package com.example.akash.coachcollab;

/**
 * Created by akash on 12/31/2017.
 */

public class TeacherTimeManager {

    private String date;
    private String toTime;
    private String fromTime;
    private String location;

    public TeacherTimeManager() {
    }

    public TeacherTimeManager(String date, String toTime, String fromTime, String location) {
        this.date = date;
        this.toTime = toTime;
        this.fromTime = fromTime;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }
}

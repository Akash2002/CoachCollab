package com.example.akash.coachcollab;

/**
 * Created by akash on 12/27/2017.
 */

public class StudentUser {

    private String description;
    private String email;
    private String name;
    private String rating;
    private String type;
    private String subject;
    private String gradeLevel;

    public StudentUser(String gradeLevel, String subject, String description, String email, String name, String rating, String type) {
        this.description = description;
        this.email = email;
        this.name = name;
        this.rating = rating;
        this.type = type;
        this.subject = subject;
        this.gradeLevel = gradeLevel;
    }

    public StudentUser() {
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String school) {
        this.subject = school;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

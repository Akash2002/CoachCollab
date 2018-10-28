package com.example.akash.coachcollab;

/**
 * Created by akash on 12/27/2017.
 */

public class UserRecyclerItem {
    private String nameHeader;
    private String subjectSideText;
    private String gradeSideText;

    public UserRecyclerItem(String nameHeader, String subjectSideText, String gradeSideText) {
        this.nameHeader = nameHeader;
        this.subjectSideText = subjectSideText;
        this.gradeSideText = gradeSideText;
    }

    public String getNameHeader() {
        return nameHeader;
    }

    public void setNameHeader(String nameHeader) {
        this.nameHeader = nameHeader;
    }

    public String getSubjectSideText() {
        return subjectSideText;
    }

    public void setSubjectSideText(String subjectSideText) {
        this.subjectSideText = subjectSideText;
    }

    public String getGradeSideText() {
        return gradeSideText;
    }

    public void setGradeSideText(String gradeSideText) {
        this.gradeSideText = gradeSideText;
    }
}

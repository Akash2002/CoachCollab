package com.example.akash.coachcollab;

/**
 * Created by akash on 12/23/2017.
 */

public class RecyclerItem {

    private String heading;
    private String description;

    public RecyclerItem(){
        this.heading = "";
        this.description = "";
    }

    public RecyclerItem(String heading, String description) {
        this.heading = heading;
        this.description = description;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

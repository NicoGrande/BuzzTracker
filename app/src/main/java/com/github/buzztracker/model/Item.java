package com.github.buzztracker.model;

import java.util.Calendar;

public class Item {

    static int idcounter = 0;
    private int id;
    private String timestamp;
    private Location location;
    private String shortDesc;
    private String fullDesc;
    private int value;
    private ItemCategory category;
    private String comment;

    public Item() {}

    public Item (Location location, String shortDesc, String fullDesc, int value, ItemCategory category,
                 String comment) {
        timestamp = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        this.location = location;
        this.shortDesc = shortDesc;
        this.fullDesc = fullDesc;
        this.value = value;
        this.category = category;
        this.comment = comment;
        id = ++idcounter;
    }

    public Item (Location location, String shortDesc, String fullDesc, int value, ItemCategory category) {
        this(location, shortDesc, fullDesc, value, category, "No comment provided");
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getFullDesc() {
        return fullDesc;
    }

    public void setFullDesc(String fullDesc) {
        this.fullDesc = fullDesc;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        String allInfo = "";
        allInfo += "Description: " + fullDesc + "\n";
        allInfo += "Category: " + category + "\n";
        allInfo += "Location: " + location.getLocationName() + "\n";
        allInfo += "Date of donation: " + timestamp + "\n";
        allInfo += "Value: $" + value + "\n" + "\n";
        allInfo += "Other Comments: \n" + comment;

        return allInfo;
    }

}

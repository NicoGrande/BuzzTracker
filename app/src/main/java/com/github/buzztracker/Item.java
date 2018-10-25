package com.github.buzztracker;

import java.util.Calendar;
import java.util.Date;

public class Item {

    private Date timestamp;
    private Location location;
    private String shortDesc;
    private String fullDesc;
    private String value;
    private ItemCategory category;
    private String comment;

    public Item (Location location, String shortDesc, String fullDesc, String value, ItemCategory category,
                 String comment) {
        timestamp = Calendar.getInstance().getTime();
        this.location = location;
        this.shortDesc = shortDesc;
        this.fullDesc = fullDesc;
        this.value = value;
        this.category = category;
        this.comment = comment;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
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
}

package com.github.buzztracker.model;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.text.DateFormat;
import java.util.Date;

import static java.text.DateFormat.getDateTimeInstance;

/**
 * Represents a donation that has been logged in the inventory
 */
public class Item {

    private static int idCounter;
    private int id;
    private String timestamp;
    private Location location;
    private String shortDesc;
    private String fullDesc;
    private int value;
    private ItemCategory category;
    private String comment;

    /**
     * Required empty constructor for Firebase
     */
    public Item() {}

    /**
     * Creates a new Item
     *
     * @param itemData an array containing the item information
     * @param comment a comment
     */
    public Item (Object[] itemData, String comment) {

        DateFormat unformattedTimestamp = getDateTimeInstance();
        Calendar calendar = Calendar.getInstance();
        Date calendarTime = calendar.getTime();
        timestamp = unformattedTimestamp.format(calendarTime);

        this.location = (Location) itemData[0];
        this.shortDesc = (String) itemData[1];
        this.fullDesc = (String) itemData[2];
        this.value = (int) itemData[3];
        this.category = (ItemCategory) itemData[4];
        this.comment = comment;

        incrementCounter();
        id = idCounter;
    }

    /**
     * Creates a new Item without a comment
     *
     * @param itemData an array containing the item information
     */
    public Item (Object[] itemData) {
        this(itemData, "No comment provided");
    }

    /**
     * Public getter for timestamp
     *
     * @return timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Public getter for location
     *
     * @return location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Public getter for shortDesc
     *
     * @return shortDesc
     */
    public String getShortDesc() {
        return shortDesc;
    }

    /**
     * Public getter for fullDesc
     *
     * @return fullDesc
     */
    String getFullDesc() {
        return fullDesc;
    }

    /**
     * Public getter for value
     *
     * @return value
     */
    public int getValue() {
        return value;
    }

    /**
     * Public getter for category
     *
     * @return category
     */
    public ItemCategory getCategory() {
        return category;
    }

    /**
     * Public getter for comment
     *
     * @return comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Public getter for id
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    @NonNull
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

    private static void incrementCounter() {
        idCounter++;
    }

    static void setIdCounter(int newCount) {
        idCounter = newCount;
    }
}

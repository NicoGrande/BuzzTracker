package com.github.buzztracker.model;

public class LocationEmployee extends User {

    private Location currentLocation;

    public LocationEmployee(String pw, String fName, String lName, String email, Long phoneNum, Location loc) {
        super(pw, fName, lName, email, phoneNum);
        currentLocation = loc;
    }
}

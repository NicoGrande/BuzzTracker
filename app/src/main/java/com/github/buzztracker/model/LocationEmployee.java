package com.github.buzztracker.model;

public class LocationEmployee extends User {

    private Location location;

    public LocationEmployee() {}

    public LocationEmployee(String pw, String fName, String lName, String email, Long phoneNum, Location loc) {
        super(pw, fName, lName, email, phoneNum);
        location = loc;
    }

    public Location getLocation() {
        return location;
    }
}

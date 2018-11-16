package com.github.buzztracker.model;

/**
 * Represents a user who works at a location
 */
public class LocationEmployee extends User {

    private Location location;

    /**
     * Empty constructor for Firebase
     */
    public LocationEmployee() {}

    /**
     * Creates a new Location Employee
     *
     * @param pw password
     * @param fName first name
     * @param lName last name
     * @param email email address
     * @param phoneNum phone number
     * @param loc location of employment
     */
    public LocationEmployee(String pw, String fName, String lName, String email, Long phoneNum,
                            Location loc) {
        super(pw, fName, lName, email, phoneNum);
        location = loc;
    }

    /**
     * Public getter for location
     *
     * @return location
     */
    public Location getLocation() {
        return location;
    }
}

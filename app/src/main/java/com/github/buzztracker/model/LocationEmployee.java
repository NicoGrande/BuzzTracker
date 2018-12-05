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
     * @param userData an array containing the user information
     */
    public LocationEmployee(Object[] userData) {
        super((String) userData[0], (String) userData[1], (String) userData[2],
                (String) userData[3], (long) userData[4]);
        location = (Location) userData[5];
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

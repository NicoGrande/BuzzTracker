package com.github.buzztracker.model;

import android.support.annotation.NonNull;

/**
 * Represents a donation location
 */
public class Location {

    private String locationName;
    private String locationType;
    private String longitude;
    private String latitude;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String phoneNumber;
    private String website;
    private String key;

    /**
     * Empty constructor for Firebase
     */
    public Location() {}

    /**
     * Creates a new Location with all its info
     *
     * @param locData an array containing the location information
     */
    public Location(String[] locData) {
        this.key = locData[0];
        locationName = locData[1];
        locationType = locData[2];
        this.longitude = locData[4];
        this.latitude = locData[3];
        this.street = locData[5];
        this.city = locData[6];
        this.state = locData[7];
        this.zipCode = locData[8];
        this.phoneNumber = locData[9];
        this.website = locData[10];
    }

    /**
     * Public getter for key
     *
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * Public getter for location name
     *
     * @return name
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * Public getter for location type
     *
     * @return type
     */
    public String getLocationType() {
        return locationType;
    }

    /**
     * Public getter for location longitude
     *
     * @return longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Public getter for location latitude
     *
     * @return latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Public getter for street
     *
     * @return street address
     */
    public String getStreet() {
        return street;
    }

    /**
     * Public getter for city
     *
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * Public getter for state
     *
     * @return state
     */
    public String getState() {
        return state;
    }

    /**
     * Public getter for zip code
     *
     * @return zip code
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Public getter for phone number
     *
     * @return phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Public getter for website
     *
     * @return website
     */
    public String getWebsite() {
        return website;
    }

    @NonNull
    public String toString() {
        String allInfo = "";
        allInfo += street + "\n" + city + ", " + state + " " + zipCode + "\n\n";
        allInfo += "Type: " + locationType + "\n";
        allInfo += "Website: " + website + "\n";
        allInfo += "Phone: " + phoneNumber + "\n";
        allInfo += "Latitude: " + latitude + "\n";
        allInfo += "Longitude: " + longitude;
        return allInfo;
    }
}

package com.github.buzztracker;

public class Location {

    private String locationName;
    private String locationType;
    private String longitude;
    private String latitude;
    private int mailboxNumber;
    private String streetName;
    private String city;
    private String state;
    private int zipCode;
    private double phoneNumber;

    public Location(String name, String type, String longitude, String latitude, int mailboxNumber,
                    String streetName, String city, String state, int zipCode, double phoneNumber) {
        locationName = name;
        locationType = type;
        this.longitude = longitude;
        this.latitude = latitude;
        this.mailboxNumber = mailboxNumber;
        this.streetName = streetName;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.phoneNumber = phoneNumber;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getMailboxNumber() {
        return mailboxNumber;
    }

    public void setMailboxNumber(int mailboxNumber) {
        this.mailboxNumber = mailboxNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public double getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(double phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

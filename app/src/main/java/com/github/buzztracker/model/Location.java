package com.github.buzztracker.model;

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

    public Location() {}

    public Location(String key, String name, String type, String latitude, String longitude,
                    String street, String city, String state, String zipCode, String phoneNumber,
                    String website) {
        this.key = key;
        locationName = name;
        locationType = type;
        this.longitude = longitude;
        this.latitude = latitude;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.phoneNumber = phoneNumber;
        this.website = website;
    }

    public String getKey() {
        return key;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

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

package com.github.buzztracker.model;

public class User {

    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private long phoneNumber;
    private boolean locked;

    public User() {}

    public User(String password, String firstName, String lastName, String email,
                long phoneNumber) {
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        locked = false;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        checkData(password);
        this.password = password;
    }

    public String getFirstName () {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        checkData(firstName);
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        checkData(lastName);
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        checkData(email);
        this.email = email;
    }

    public long getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean getLocked() {
        return locked;
    }

    void setLocked(boolean status) {
        locked = status;
    }

    public void checkData(String dataIn) {
        if (dataIn == null) {
            throw new IllegalArgumentException("Invalid input data.");
        }
    }
}
package com.github.buzztracker.model;

/**
 * Represents a generic user of the app
 */
public class User {

    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private long phoneNumber;
    private boolean locked;

    /**
     * Empty constructor for Firebase
     */
    public User() {}

    /**
     * Creates a new user
     *
     * @param password password
     * @param firstName first name
     * @param lastName last name
     * @param email email address
     * @param phoneNumber phone number
     */
    public User(String password, String firstName, String lastName, String email,
                long phoneNumber) {
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        locked = false;
    }

    /**
     * Public getter for password
     *
     * @return password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Public getter for first name
     *
     * @return first name
     */
    public String getFirstName () {
        return this.firstName;
    }

    /**
     * Public getter for last name
     *
     * @return last name
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Public getter for email
     *
     * @return email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Public getter for phone number
     *
     * @return phone number
     */
    public long getPhoneNumber() {
        return this.phoneNumber;
    }

    /**
     * Returns the locked status of the user
     *
     * @return true is the account is locked, false if unlocked
     */
    public boolean getLocked() {
        return locked;
    }

    /**
     * Sets the locked status of the user
     *
     * @param status the new locked status
     */
    void setLocked(boolean status) {
        locked = status;
    }
}
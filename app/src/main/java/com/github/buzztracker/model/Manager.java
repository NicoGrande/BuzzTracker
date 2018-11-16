package com.github.buzztracker.model;

/**
 * Represents a user who is a manager
 */
public class Manager extends User {

    /**
     * Empty constructor for Firebase
     */
    public Manager() {}

    /**
     * Creates a new Manager
     *
     * @param pw password
     * @param fName first name
     * @param lName last name
     * @param email email address
     * @param phoneNum phone number
     */
    public Manager(String pw, String fName, String lName, String email, Long phoneNum) {
        super(pw, fName, lName, email, phoneNum);
    }
}

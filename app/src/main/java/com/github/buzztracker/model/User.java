package com.github.buzztracker.model;

import java.util.HashMap;

public class User {

    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private long phoneNumber;
    private int employeeID;

    // email to password mapping for login until Firebase is setup
    public static HashMap<String, String> credentials = new HashMap<>();

    // email to user mapping for user lookup until Firebase is setup
    public static HashMap<String, User> users = new HashMap<>();

    public User(String PW, String firstName, String lastName, String email,
                long phoneNumber) {

        password = PW;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;

        credentials.put(email, password);
    }

    public String getPassWord() {
        return this.password;
    }

    public void setPassWord(String passWord) {
        checkData(passWord);
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

    public int getEmployeeID() {
        return this.employeeID;
    }

    public void setEmployeeID(long phoneNumber) {
        this.employeeID = employeeID;
    }

    public void checkData(String dataIn) {
        if (dataIn == null) {
            throw new IllegalArgumentException("Invalid input data.");
        }
    }

}
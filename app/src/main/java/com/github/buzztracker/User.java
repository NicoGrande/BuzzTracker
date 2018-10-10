package com.github.buzztracker;

import java.util.HashMap;

public class User {

    private String userName;
    private String passWord;
    private String firstName;
    private String lastName;
    private String email;
    private long phoneNumber;
    private int employeeID;

    // email to password mapping for login
    public static HashMap<String, String> credentials = new HashMap<>();

    // email to user mapping for user lookup
    public static HashMap<String, User> users = new HashMap<>();

    public User(String UN, String PW, String firstName, String lastName, String email,
                long phoneNumber, int employeeID) {

        userName = UN;
        passWord = PW;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.employeeID = employeeID;

        credentials.put(userName, passWord);
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        checkData(userName);
        this.userName = userName;
    }

    public String getPassWord() {
        return this.passWord;
    }

    public void setPassWord(String passWord) {
        checkData(passWord);
        this.passWord = passWord;
    }

    public String getFirstName () {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        checkData(firstName);
        this.userName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        checkData(lastName);
        this.userName = lastName;
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
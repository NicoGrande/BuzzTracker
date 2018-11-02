package com.github.buzztracker.model;

public class Admin extends User {

    public Admin() {}

    public Admin(String pw, String fName, String lName, String email, Long phoneNum) {
        super(pw, fName, lName, email, phoneNum);
    }

    public void lockAcct(User user) {
        user.setLocked(true);
    }

    public void unlockAcct(User user) {
        user.setLocked(false);
    }
}

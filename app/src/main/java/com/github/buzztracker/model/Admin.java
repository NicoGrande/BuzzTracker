package com.github.buzztracker.model;

public class Admin extends User {

    public Admin(String pw, String fName, String lName, String email, Long phoneNum) {
        super(pw, fName, lName, email, phoneNum);
    }

    public void lockAcct(User user) {
        user.locked = true;
    }

    public void unlockAcct(User user) {
        user.locked = false;
    }
}

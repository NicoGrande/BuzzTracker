package com.github.buzztracker;

public class Admin extends User {

    private boolean locked = false;

    public Admin(User user) {
        super(user.getPassWord(), user.getFirstName(), user.getLastName(), user.getEmail(),
                user.getPhoneNumber(), user.getEmployeeID());
    }

    public String itemSearch(String query) {
        return null;
    }

    public String locationSearch(String query) {
        return null;
    }

    public User registerUser(User user) {
        return null;
    }

    public void lockAcct(User user) {
        if (!locked) {
            locked = true;
        }
    }

    public void unlockAcct(User user) {
        if (locked) {
            locked = false;
        }
    }
}

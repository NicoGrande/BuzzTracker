package com.github.buzztracker.model;

/**
 * Represents a user with admin privileges
 */
public class Admin extends User {

    /**
     * Empty constructor required by Firebase
     */
    public Admin() {}

    Admin(String password, String firstName, String lastName, String email, Long phoneNum) {
        super(password, firstName, lastName, email, phoneNum);
    }

    void lockAcct(User user) {
        user.setLocked(true);
    }

    void unlockAcct(User user) {
        user.setLocked(false);
    }
}

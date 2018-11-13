package com.github.buzztracker;

import com.github.buzztracker.model.Admin;
import com.github.buzztracker.model.Location;
import com.github.buzztracker.model.LocationEmployee;
import com.github.buzztracker.model.Manager;
import com.github.buzztracker.model.User;
import com.github.buzztracker.model.Model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests Model class methods
 */
public class ModelTest {

    Model model;

    @Before
    public void atStart() {
        model = Model.getInstance();
    }

    /**
     * Bradley Dover's JUnit test on method Model.createUser(String, String, String,
     *                                                       String, Long, String, Location)
     */
    @Test
    public void testCreateUser() {
        User user;
        final String password = "Password123";
        final String firstName = "Aaron";
        final String lastName = "Yates";
        final String email = "im.a.legal.email@email.com";
        final long phoneNumber = 1234567890;
        final Location location = new Location();
        final String[] userTypes = {"User", "Location Employee", "Admin", "Manager"};
        final String illegalUserType = "Other";
        final Class[] classes = {User.class, LocationEmployee.class, Admin.class, Manager.class};

        for (int i = 0; i < userTypes.length; i++) {
            user = model.createUser(password, firstName, lastName, email, phoneNumber, userTypes[i], location);
            assertTrue("Created user of incorrect type", user.getClass().equals(classes[i]));
            assertEquals("Provided password is incorrect", password, user.getPassword());
            assertEquals("Provided first name is incorrect", firstName, user.getFirstName());
            assertEquals("Provided last name is incorrect", lastName, user.getLastName());
            assertEquals("Provided email is incorrect", email, user.getEmail());
            assertEquals("Provided phone number is incorrect", phoneNumber, user.getPhoneNumber());
            if (user instanceof LocationEmployee) {
                assertEquals("Provided location is incorrect", location, ((LocationEmployee) user).getLocation());
            }
        }

        try {
            user = model.createUser(password, firstName, lastName, email, phoneNumber, illegalUserType, location);
            fail("Did not throw IllegalArgumentException when creating user with illegal user type");
        } catch (IllegalArgumentException ignored) {}
    }
}
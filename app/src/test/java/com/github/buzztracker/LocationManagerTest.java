package com.github.buzztracker;

import com.github.buzztracker.model.Location;
import com.github.buzztracker.model.LocationManager;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LocationManagerTest {

    /**
     * Avani Pavuluri's JUnit test on static method LocationManager.getLocationFromName(String)
     */
    @Test
    public void testGetLocationFromName() {

        String locationName = "Atlanta Location";

        Location location = new Location("1", locationName, "pickup", "60", "130", "12 Peachtree",
                "Atlanta", "Georgia", "30309", "6096728733", "www.google.com");

        Location result = LocationManager.getLocationFromName("AFD Station");
        assertNull("Method did not return null on empty location list", result);

        LocationManager.addLocation(location);
        result = LocationManager.getLocationFromName("String that isn't the name of a location");
        assertNull("Provided location is incorrect", result);

        result = LocationManager.getLocationFromName(locationName);
        assertEquals("Did not return correct location", location, result);
    }
}

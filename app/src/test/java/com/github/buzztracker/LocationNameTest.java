package com.github.buzztracker;

import com.github.buzztracker.model.LocationManager;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LocationNameTest {

    /**
     * Kevin Kelly's JUnit test on static method LocationManager.getLocationNames()
     */
    @Test
    public void testGetLocationNames() {
        List<String> expectedLocNames = new ArrayList<>();
        expectedLocNames.add("Atlanta Location");
        expectedLocNames.add("BOYS & GILRS CLUB W.W. WOOLFOLK");
        expectedLocNames.add("PATHWAY UPPER ROOM CHRISTIAN MINISTRIES");
        expectedLocNames.add("PAVILION OF HOPE INC");
        expectedLocNames.add("D&D CONVENIENCE STORE");
        expectedLocNames.add("KEEP NORTH FULTON BEAUTIFUL");
        List<String> actualLocNames = LocationManager.getLocationNames();
        for (int i = 0; i < actualLocNames.size(); i++) {
            assertEquals("Names of all Locations not properly received", expectedLocNames.get(i), actualLocNames.get(i));
        }
    }
}

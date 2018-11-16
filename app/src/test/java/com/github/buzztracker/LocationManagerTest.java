package com.github.buzztracker;

import com.github.buzztracker.model.CSVReader;
import com.github.buzztracker.model.Location;
import com.github.buzztracker.model.LocationManager;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LocationManagerTest {

    private LocationManager locationManager;

    @Before
    public void atStart() {
        // Mimics locations.csv read since context is needed for getResources()
        String rawCSV = "Key,Name,Latitude,Longitude,Street Address,City,State,Zip,Type,Phone,Website\n" +
        "1,AFD Station 4,33.75416,-84.37742,309 EDGEWOOD AVE SE,Atlanta,GA,30332,Drop Off,(404) 555 - 3456,www.afd04.atl.ga\n" +
        "2,BOYS & GILRS CLUB W.W. WOOLFOLK,33.73182,-84.43971,1642 RICHLAND RD,Atlanta,GA,30332,Store,(404) 555 - 1234,www.bgc.wool.ga\n" +
        "3,PATHWAY UPPER ROOM CHRISTIAN MINISTRIES,33.70866,-84.41853,1683 SYLVAN RD,Atlanta,GA,30332,Warehouse,(404) 555 - 5432,www.pathways.org\n" +
        "4,PAVILION OF HOPE INC,33.80129,-84.25537,3558 EAST PONCE DE LEON AVE,SCOTTDALE,GA,30079,Warehouse,(404) 555 - 8765,www.pavhope.org\n" +
        "5,D&D CONVENIENCE STORE,33.71747,-84.2521,2426 COLUMBIA DRIVE,DECATUR,GA,30034,Drop Off,(404) 555 - 9876,www.ddconv.com\n" +
        "6,KEEP NORTH FULTON BEAUTIFUL,33.96921,-84.3688,470 MORGAN FALLS RD,Sandy Springs,GA,30302,Store,(770) 555 - 7321,www.knfb.org";
        InputStream stream = new ByteArrayInputStream(rawCSV.getBytes(StandardCharsets.UTF_8));

        locationManager = LocationManager.getInstance();

        // Populates locations with info from above
        CSVReader.parseCSV(stream, locationManager);
    }

    /**
     * Avani Pavuluri's JUnit test on method LocationManager.getLocationFromName(String)
     */
    @Test
    public void testGetLocationFromName() {

        Location result;
        // Gets a real location name
        String locationName = locationManager.getLocationNames().get(0);

        // When the name does not match a locations
        result = locationManager.getLocationFromName("String that isn't the name of a location");
        assertNull("Provided location on invalid name", result);

        // When there is a location match for the name
        result = locationManager.getLocationFromName(locationName);
        assertEquals("Did not return correct location", locationManager.getLocations().get(0), result);
    }

    /**
     * Rafael Gandra's JUnit test on method LocationManager.getLocationCoords()
     */
    @Test
    public void testGetLocationCoords() {

        // Todo: Mimic the format that Kevin did below
        // Your test doesn't actually call getLocationCoords() which means it can't be testing it
        // Start by generating a List of LatLngs that would be expected from every location
        // You will either need to reference res/raw/locations.csv or the app location list UI screens to get the coordinates
        List<LatLng> expectedCoords = new ArrayList<>();
        expectedCoords.add(new LatLng(33.75416, -84.37742)); // LatLng for location 1
        // Add the other 5 locations' LatLngs here into expectedCoords

        // Next, call the method and store the returned results in another List. This is the actual result

        // To verify the correctness, iterate through the lists together
        // and assert that expectedCoords[i] equals actualCoords[i] for each i

        // Todo: Add another check here to verify that the method returns null if the location list is empty
        locationManager.clearLocations();


        // Last, remove everything below here. I hope this helps
        String locationName = "Location";
        String[] lat = {"78", "45","60","91","23"};
        String[] lng = {"43", "39","55","74","82"};

        for (int i = 0; i < lat.length; i++) {

            Location location = new Location("3", locationName, "pickup", lat[i], lng[i], "930 Spring",
                    "Atlanta", "Georgia", "30309", "4049012977", "www.gatech.edu");

            locationManager.addLocation(location);

            assertEquals("Did not return correct location", location, (new LatLng(Double.parseDouble(lat[i]),
                    Double.parseDouble(lng[i]))));

        }
    }

    /**
     * Kevin Kelly's JUnit test on method LocationManager.getLocationNames()
     */
    @Test
    public void testGetLocationNames() {

        // Tests results when location list is populated
        List<String> expectedLocNames = new ArrayList<>();
        expectedLocNames.add("AFD Station 4");
        expectedLocNames.add("BOYS & GILRS CLUB W.W. WOOLFOLK");
        expectedLocNames.add("PATHWAY UPPER ROOM CHRISTIAN MINISTRIES");
        expectedLocNames.add("PAVILION OF HOPE INC");
        expectedLocNames.add("D&D CONVENIENCE STORE");
        expectedLocNames.add("KEEP NORTH FULTON BEAUTIFUL");
        List<String> actualLocNames = locationManager.getLocationNames();
        for (int i = 0; i < expectedLocNames.size(); i++) {
            assertEquals("Names of all Locations not properly received", expectedLocNames.get(i), actualLocNames.get(i));
        }

        // Todo: Add a test that checks if null is returned on an empty location list
        // Tests results when location list is empty
        locationManager.clearLocations();

    }
}

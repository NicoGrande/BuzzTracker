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
        CSVReader csvReader = new CSVReader();
        csvReader.parseCSV(stream, locationManager);
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

        // Generating a List of LatLngs that would be expected from every location
        List<LatLng> expectedCoords = new ArrayList<>();
        expectedCoords.add(new LatLng(33.75416, -84.37742)); // LatLng for location 1
        expectedCoords.add(new LatLng(33.73182, -84.43971)); // LatLng for location 2
        expectedCoords.add(new LatLng(33.70866, -84.41853)); // LatLng for location 3
        expectedCoords.add(new LatLng(33.80129, -84.25537)); // LatLng for location 4
        expectedCoords.add(new LatLng(33.71747, -84.2521)); // LatLng for location 5
        expectedCoords.add(new LatLng(33.96921, -84.3688)); // LatLng for location 6

        List<LatLng> actualLocCoords = locationManager.getLocationCoords();

        // To verify the correctness, iterate through the lists together
        // and assert that expectedCoords[i] equals actualCoords[i] for each i
        for (int i = 0; i < expectedCoords.size(); i++) {
            assertEquals("Coordinates of all Locations not properly received", expectedCoords.get(i), actualLocCoords.get(i));
        }
        locationManager.clearLocations();
        actualLocCoords = locationManager.getLocationCoords();
        assertNull("Did not return null on empty location list", actualLocCoords);
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

        // Tests results when location list is empty
        locationManager.clearLocations();
        actualLocNames = locationManager.getLocationNames();
        assertNull("Did not return null on empty location list", actualLocNames);
    }
}

package com.github.buzztracker;

        import com.github.buzztracker.model.Location;
        import com.github.buzztracker.model.LocationManager;
        import com.google.android.gms.maps.model.LatLng;

        import org.junit.Test;

        import static org.junit.Assert.assertEquals;
        import static org.junit.Assert.assertNull;

public class LocationCoord {

    /**
     * Rafael Gandra's JUnit test on static method LocationManager.getLocationCoord(String)
     */
    @Test
    public void testGetLocationCoord() {

        String locationName = "Location";
        String[] lat = {"78", "45","60","91","23"};
        String[] lng = {"43", "39","55","74","82"};

        for (int i = 0; i < lat.length; i++) {

            Location location = new Location("3", locationName, "pickup", lat[i], lng[i], "930 Spring",
                    "Atlanta", "Georgia", "30309", "4049012977", "www.gatech.edu");

            LocationManager.addLocation(location);

            assertEquals("Did not return correct location", location, (new LatLng(Double.parseDouble(lat[i]),
                    Double.parseDouble(lng[i]))));

        }
    }
}


package com.github.buzztracker;

import java.util.ArrayList;
import java.util.List;

public class LocationManager {
    private static List<Location> locations = new ArrayList<>();

    public static void addLocation(Location loc) {
        locations.add(loc);
    }

    public static List<Location> getLocations() {
        return locations;
    }
}

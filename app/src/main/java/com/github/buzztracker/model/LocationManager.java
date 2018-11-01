package com.github.buzztracker.model;

import com.github.buzztracker.model.Location;

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

    public static List<String> getLocationNames() {
        List<String> locationNames = new ArrayList<>();
        for (Location l : locations) {
            locationNames.add(l.getLocationName());
        }
        return locationNames;
    }

    // Returns null if location not found
    public static Location getLocationFromName(String name) {
        for (Location loc : locations) {
            if (loc.getLocationName().equals(name)) {
                return loc;
            }
        }
        return null;
    }

    public static void clearLocations() {
        locations.clear();
    }
}

package com.github.buzztracker.model;

import com.google.android.gms.maps.model.LatLng;

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

    public static List<LatLng> getLocationCoords() {
        List<LatLng> locationCoords = new ArrayList<>();
        for (Location location : locations) {
            locationCoords.add(new LatLng(Double.parseDouble(location.getLatitude()),
                    Double.parseDouble(location.getLongitude())));
        }
        return locationCoords;
    }

    public static void clearLocations() {
        locations.clear();
    }


}

package com.github.buzztracker.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds information about all known locations
 */
public class LocationManager {

    /**
     * Provides access to an instance of the location manager
     *
     * @return an instance of LocationManager
     */
    public static LocationManager getInstance() {
        return new LocationManager();
    }

    private final List<Location> locations = new ArrayList<>();

    /**
     * Adds a new location to the location list
     *
     * @param loc new location to add
     */
    public void addLocation(Location loc) {
        locations.add(loc);
    }

    /**
     * Public getter for location list
     *
     * @return list of all locations
     */
    public List<Location> getLocations() {
        return Collections.unmodifiableList(locations);
    }

    /**
     * Public getter for location list as strings
     *
     * @return list of all locations as strings; null if empty
     */
    public List<String> getLocationNames() {
        List<String> locationNames = new ArrayList<>();
        for (Location l : locations) {
            locationNames.add(l.getLocationName());
        }
        return locationNames;
    }

    /**
     * Determines which Location corresponds to passed in String of location name
     *
     * @param name the name of the desired Location
     * @return the Location with the name that matches name, null otherwise
     */
    public Location getLocationFromName(String name) {
        for (Location loc : locations) {
            String locationName = loc.getLocationName();
            if (locationName.equals(name)) {
                return loc;
            }
        }
        return null;
    }

    /**
     * Public getter for location coordinates for use in the map
     *
     * @return list of all location coordinates; null if empty
     */
    public List<LatLng> getLocationCoords() {
        List<LatLng> locationCoords = new ArrayList<>();
        for (Location location : locations) {
            locationCoords.add(new LatLng(Double.parseDouble(location.getLatitude()),
                    Double.parseDouble(location.getLongitude())));
        }
        return locationCoords;
    }

    /**
     * Removes all locations from the list
     */
    public void clearLocations() {
        locations.clear();
    }


}

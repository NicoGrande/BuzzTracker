package com.github.buzztracker.model;

import android.util.Log;

import com.github.buzztracker.model.Location;
import com.github.buzztracker.model.LocationManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

class CSVReader {

    /**
     * Reads in location info from a CSV file and adds locations to the location list
     *
     * @param input a string of comma separated strings with which to build the locations
     */
    static void parseCSV(InputStream input, LocationManager locationManager) {
        try {
            BufferedReader data = new BufferedReader(new InputStreamReader(input,
                    StandardCharsets.UTF_8));

            String line;
            data.readLine(); //get rid of header line
            line = data.readLine();
            locationManager.clearLocations();
            while (line != null) {
                String[] tokens = line.split(",");
                locationManager.addLocation(new Location(tokens[0], tokens[1], tokens[8],
                        tokens[2], tokens[3], tokens[4], tokens[5], tokens[6], tokens[7],
                        tokens[9], tokens[10]));
                line = data.readLine();
            }
            data.close();
        } catch (IOException e) {
            Log.e("CSV Parsing", "Error reading CSV");
        }
    }
}

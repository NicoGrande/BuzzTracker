package com.github.buzztracker.model;

import android.util.Log;

import com.github.buzztracker.model.Location;
import com.github.buzztracker.model.LocationManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CSVReader {

    public static void parseCSV(InputStream input) {
        try {
            BufferedReader data = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));

            String line;
            data.readLine(); //get rid of header line
            LocationManager.clearLocations();
            while ((line = data.readLine()) != null) {
                String[] tokens = line.split(",");
                LocationManager.addLocation(new Location(tokens[0], tokens[1], tokens[8], tokens[2], tokens[3], tokens[4], tokens[5], tokens[6], tokens[7], tokens[9], tokens[10]));
            }
            data.close();
        } catch (IOException e) {
            Log.e("CSV Parsing", "Error reading CSV");
    }
    }
}

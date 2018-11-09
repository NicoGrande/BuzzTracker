package com.github.buzztracker.controllers;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.github.buzztracker.R;
import com.github.buzztracker.model.Location;
import com.github.buzztracker.model.LocationManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Location> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locations = LocationManager.getLocations();
        Location location;

        // Add marker for AFD Station and move the camera
        location = locations.get(0);
        LatLng AFDStation = new LatLng(Double.parseDouble(location.getLatitude()),
                Double.parseDouble(location.getLongitude()));
        mMap.addMarker(new MarkerOptions().position(AFDStation).title("AFD Station"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(AFDStation));

//        // Add marker for Boys and Girls Club
//        location = locations.get(1);
//        LatLng boysAndGirlsClub = new LatLng(Double.parseDouble(location.getLatitude()),
//                Double.parseDouble(location.getLongitude()));
//        mMap.addMarker(new MarkerOptions().position(boysAndGirlsClub).title("Boys & Girls Club"));
//
//        // Add marker for Pathway Upper Room Christian Ministries
//        location = locations.get(2);
//        LatLng pathwayUpperRoomMinistries = new LatLng(Double.parseDouble(location.getLatitude()),
//                Double.parseDouble(location.getLongitude()));
//        mMap.addMarker(new MarkerOptions().position(pathwayUpperRoomMinistries)
//                .title("Pathway Upper Room Christian Ministries"));
//
//        // Add marker for Pavilion of Hope
//        location = locations.get(3);
//        LatLng pavilionOfHope = new LatLng(Double.parseDouble(location.getLatitude()),
//                Double.parseDouble(location.getLongitude()));
//        mMap.addMarker(new MarkerOptions().position(pavilionOfHope).title("Pavilion of Hope"));
//
//        // Add marker for D&D Convenience Store
//        location = locations.get(4);
//        LatLng ddConvenienceStore = new LatLng(Double.parseDouble(location.getLatitude()),
//                Double.parseDouble(location.getLongitude()));
//        mMap.addMarker(new MarkerOptions().position(ddConvenienceStore).title("D&D Convenience Store"));
//
//        // Add marker for Keep North Fulton Beautiful
//        location = locations.get(5);
//        LatLng keepNorthFultonBeautiful = new LatLng(Double.parseDouble(location.getLatitude()),
//                Double.parseDouble(location.getLongitude()));
//        mMap.addMarker(new MarkerOptions().position(keepNorthFultonBeautiful).title("Keep North Fulton Beautiful"));
    }
}

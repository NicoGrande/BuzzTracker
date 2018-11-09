package com.github.buzztracker.controllers;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.github.buzztracker.R;
import com.github.buzztracker.model.Location;
import com.github.buzztracker.model.Model;
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
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = Model.getInstance();
        model.updateContext(this);

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
        mMap.getUiSettings().setZoomControlsEnabled(true);


        locations = model.getLocations();

        // Add marker for each location and move the camera
        for (Location location : locations) {
            LatLng locationCoords = new LatLng(Double.parseDouble(location.getLatitude()),
                    Double.parseDouble(location.getLongitude()));
            mMap.addMarker(new MarkerOptions()
                    .position(locationCoords)
                    .title(location.getLocationName())
                    .snippet(location.getPhoneNumber()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationCoords, 10.0f));
        }
    }
}

package com.github.buzztracker.controllers;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.github.buzztracker.R;
import com.github.buzztracker.model.Location;
import com.github.buzztracker.model.Model;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Displays the locations on a Google Map
 */
public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = Model.getInstance();

        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        FragmentManager fragmentManager = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager
                .findFragmentById(R.id.map);
        assert mapFragment != null;
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
        UiSettings uiSettings= googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);


        List<Location> locations = model.getLocations();
        List<LatLng> locationCoords = model.getLocationCoords();

        final float DEFAULT_ZOOM = 10.0f;

        // Add marker for each location and move the camera
        for (int i = 0; i < locationCoords.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(locationCoords.get(i));
            Location location = locations.get(i);
            markerOptions.title(location.getLocationName());
            markerOptions.snippet(location.getPhoneNumber());
            googleMap.addMarker(markerOptions);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationCoords.get(i),
                    DEFAULT_ZOOM));
        }
    }
}

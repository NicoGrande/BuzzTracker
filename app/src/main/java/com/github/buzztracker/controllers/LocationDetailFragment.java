package com.github.buzztracker.controllers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.buzztracker.R;
import com.github.buzztracker.model.Location;
import com.github.buzztracker.model.Model;

import java.util.List;

/**
 * A fragment representing a single Location detail screen.
 * This fragment is either contained in a {@link LocationListActivity}
 * in two-pane mode (on tablets) or a {@link LocationDetailActivity}
 * on handsets.
 */
public class LocationDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Location location;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LocationDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Model model = Model.getInstance();

        assert getArguments() != null;
        Bundle bundle = getArguments();
        if (bundle.containsKey(ARG_ITEM_ID)) {
            String bundleString = bundle.toString();
            int idIndex = bundleString.indexOf("=");
            idIndex++;
            List<Location> locations = model.getLocations();
            location = locations.get((Integer.parseInt(
                    bundleString.substring(idIndex, idIndex + 1))) - 1);

            Activity activity = this.getActivity();
            assert activity != null;
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(location.getLocationName());
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.location_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (location != null) {
            ((TextView) rootView.findViewById(R.id.location_detail)).setText(location.toString());
        }

        return rootView;
    }
}

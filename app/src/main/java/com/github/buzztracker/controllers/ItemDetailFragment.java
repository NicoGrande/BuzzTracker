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
import com.github.buzztracker.model.Item;
import com.github.buzztracker.model.Model;

import java.util.List;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Item item;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Model model = Model.getInstance();
        Bundle args = getArguments();

        if ((args != null) && args.containsKey(ARG_ITEM_ID)) {
            String bundleString = args.toString();
            int idIndex = bundleString.indexOf("=");
            idIndex++;
            List<Item> inventory = model.getInventory();
            item = inventory.get((Integer.parseInt(
                    bundleString.substring(idIndex, idIndex + 1))) - 1);

            Activity activity = this.getActivity();
            assert activity != null;
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(item.getShortDesc());
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (item != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(item.toString());
        }

        return rootView;
    }
}

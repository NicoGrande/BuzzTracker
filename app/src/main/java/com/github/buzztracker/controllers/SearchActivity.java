package com.github.buzztracker.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.buzztracker.R;
import com.github.buzztracker.model.Inventory;
import com.github.buzztracker.model.Item;
import com.github.buzztracker.model.ItemCategory;
import com.github.buzztracker.model.Location;
import com.github.buzztracker.model.LocationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    // UI references
    private EditText searchTextView;
    private Spinner categorySpinner;
    private Spinner locationSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchTextView = findViewById(R.id.search_text);

        categorySpinner = findViewById(R.id.search_category_spinner);

        // Populate a list of the possible item categories along with an "All categories" option
        List<String> adapterCategories = new ArrayList<>();
        adapterCategories.add("Search all categories");
        for (ItemCategory cat : ItemCategory.values()) {
            adapterCategories.add(cat.getAsString());
        }

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, adapterCategories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        locationSpinner = findViewById(R.id.search_location_spinner);

        // Populate a list of the possible locations along with an "All locations" option
        List<String> adapterLocations = new ArrayList<>();
        adapterLocations.add("Search all locations");
        for (Location loc : LocationManager.getLocations()) {
            adapterLocations.add(loc.getLocationName());
        }

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(this,
        android.R.layout.simple_spinner_item, adapterLocations);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);

        Button cancelButton = findViewById(R.id.search_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchActivity.this, MainScreenActivity.class);
                SearchActivity.this.startActivity(i);
            }
        });

        Button searchKeywordButton = findViewById(R.id.search_keyword_button);
        searchKeywordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchByKeyword();
            }
        });

        Button sortCategoryButton = findViewById(R.id.search_category_button);
        sortCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByCategory();
            }
        });
    }

    private void searchByKeyword() {
        String searchKeywords = searchTextView.getText().toString().trim();
        String location = locationSpinner.getSelectedItem().toString();

        String[] keywords = searchKeywords.split("\\s+");
        String[] itemDescriptionWords;
        List<Item> filteredItems = new ArrayList<>();

        if (location.equals("Search all locations")) {
            for (Item item : Inventory.getInventory()) {
                itemDescriptionWords = getDescriptionKeywords(item);
                outerLoop:
                for (int i = 0; i < keywords.length; i++) {
                    for (int j = 0; j < itemDescriptionWords.length; j++) {
                        if (keywords[i].equals(itemDescriptionWords[j])) {
                            filteredItems.add(item);
                            break outerLoop;
                        }
                    }
                }
            }
        } else {
            for (Item item : Inventory.getInventory()) {
                if (item.getLocation().getLocationName().equals(location)) {
                    itemDescriptionWords = getDescriptionKeywords(item);
                    outerLoop:
                    for (int i = 0; i < keywords.length; i++) {
                        for (int j = 0; j < itemDescriptionWords.length; j++) {
                            if (keywords[i].equals(itemDescriptionWords[j])) {
                                filteredItems.add(item);
                                break outerLoop;
                            }
                        }
                    }
                }
            }
        }
        displayResults(filteredItems);
    }

    private void sortByCategory() {
        ItemCategory category = getCategory(categorySpinner.getSelectedItem().toString());
        String location = locationSpinner.getSelectedItem().toString();

        List<Item> filteredItems = new ArrayList<>();

        if (location.equals("Search all locations")) {
            for (Item item : Inventory.getInventory()) {
                // if searching all categories
                if (category == null) {
                    filteredItems.add(item);
                } else if (item.getCategory() == category) {
                    filteredItems.add(item);
                }
            }
        } else {
            for (Item item : Inventory.getInventory()) {
                if (item.getLocation().getLocationName().equals(location)) {
                    if (category == null) {
                        filteredItems.add(item);
                    } else if (item.getCategory() == category) {
                        filteredItems.add(item);
                    }
                }
            }
        }
        displayResults(filteredItems);
    }

    private String[] getDescriptionKeywords(Item item) {
        String description = item.getFullDesc() + " " + item.getShortDesc();
        return description.split("\\s+");
    }

    private ItemCategory getCategory(String cat) {
        if (cat.equals("Search all categories")) {
            return null;
        }
        for (ItemCategory ic : ItemCategory.values()) {
            if (ic.getAsString().equals(cat)) {
                return ic;
            }
        }
        return null;
    }

    private void displayResults(List<Item> items) {
        if (items.size() == 0) {
            Toast.makeText(this, "No items match search criteria", Toast.LENGTH_LONG).show();
        } else {
            Inventory.setFilteredInventory(items);
            Intent i = new Intent(SearchActivity.this, SearchListActivity.class);
            SearchActivity.this.startActivity(i);
        }
    }
}

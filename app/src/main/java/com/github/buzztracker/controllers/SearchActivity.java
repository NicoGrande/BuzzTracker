package com.github.buzztracker.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.github.buzztracker.R;
import com.github.buzztracker.model.ItemCategory;
import com.github.buzztracker.model.Location;
import com.github.buzztracker.model.Model;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    // UI references
    private EditText searchTextView;
    private Spinner categorySpinner;
    private Spinner locationSpinner;

    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchTextView = findViewById(R.id.search_text);

        categorySpinner = findViewById(R.id.search_category_spinner);

        model = Model.getInstance();
        model.updateContext(this);

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
        for (Location loc : model.getLocations()) {
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
                searchByCategory();
            }
        });
    }

    private void searchByKeyword() {
        String searchKeywords = searchTextView.getText().toString().trim();
        String location = locationSpinner.getSelectedItem().toString();
        model.searchByKeyword(searchKeywords, location);
    }

    private void searchByCategory() {
        ItemCategory category = model.getCategory(categorySpinner.getSelectedItem().toString());
        String location = locationSpinner.getSelectedItem().toString();
        model.searchByCategory(category, location);
    }
}

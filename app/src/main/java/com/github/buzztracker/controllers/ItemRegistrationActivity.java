package com.github.buzztracker.controllers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.buzztracker.model.Location;
import com.github.buzztracker.model.LocationManager;
import com.github.buzztracker.R;
import com.github.buzztracker.model.Inventory;
import com.github.buzztracker.model.Item;
import com.github.buzztracker.model.ItemCategory;

public class ItemRegistrationActivity extends AppCompatActivity {

    // UI references
    private EditText shortDescView;
    private EditText longDescView;
    private EditText valueView;
    private EditText commentView;
    private Spinner categorySpinner;
    private Spinner locationSpinner;

    // Allows hiding Add Item screen to show loading UI
    private View addItemView;
    private View progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_registration);

        // Cancel button
        Button addItemCancelButton = findViewById(R.id.item_cancel);
        addItemCancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(ItemRegistrationActivity.this, ItemListActivity.class);
                ItemRegistrationActivity.this.startActivity(i);
            }
        });

        // Add item button
        Button addItemCompleteButton = findViewById(R.id.item_create);
        addItemCompleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                attemptCreateItem();
            }
        });

        categorySpinner = findViewById(R.id.item_category);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, ItemCategory.values());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        locationSpinner = findViewById(R.id.item_location);
        ArrayAdapter<String> locationAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, LocationManager.getLocationNames());
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);

        // UI fields
        shortDescView = findViewById(R.id.item_short_desc);
        longDescView = findViewById(R.id.item_desc_long);
        valueView = findViewById(R.id.item_value);
        commentView = findViewById(R.id.item_comment);

        addItemView = findViewById(R.id.item_add_form);
        progressView = findViewById(R.id.item_add_progress);
    }

    private void attemptCreateItem() {
        // Reset errors
        shortDescView.setError(null);
        longDescView.setError(null);
        valueView.setError(null);
        commentView.setError(null);

        String shortDesc = shortDescView.getText().toString().trim();
        String longDesc = longDescView.getText().toString().trim();
        String value = valueView.getText().toString().trim();
        String comment = commentView.getText().toString().trim();
        String location = locationSpinner.getSelectedItem().toString();
        ItemCategory category = (ItemCategory) categorySpinner.getSelectedItem();

        // Allows us to cancel registration request if a field is invalid
        boolean cancel = false;
        View focusView = null;

        // Value verification
        if (TextUtils.isEmpty(value)) {
            valueView.setError(getString(R.string.error_field_required));
            focusView = valueView;
            cancel = true;
        } else {
            int i;
            try {
                i = Integer.parseInt(value);
                if (i < 0) {
                    valueView.setError(getString(R.string.error_invalid_cost));
                    focusView = valueView;
                    cancel = true;
                }
            } catch (NumberFormatException e) {
                valueView.setError(getString(R.string.error_invalid_cost));
                focusView = valueView;
                cancel = true;
            }

        }

        // Long description verification
        if (TextUtils.isEmpty(longDesc)) {
            longDescView.setError(getString(R.string.error_field_required));
            focusView = longDescView;
            cancel = true;
        }

        // Short description verification
        if (TextUtils.isEmpty(shortDesc)) {
            shortDescView.setError(getString(R.string.error_field_required));
            focusView = shortDescView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);

            Location loc = LocationManager.getLocationFromName(location);
            Item item;
            if (comment.isEmpty()) {
                item = new Item(loc, shortDesc, longDesc, Integer.parseInt(value), category);
            } else {
                item = new Item(loc, shortDesc, longDesc, Integer.parseInt(value), category, comment);
            }
            Inventory.addToInventory(item);

            Toast.makeText(ItemRegistrationActivity.this, "Item successfully added to inventory", Toast.LENGTH_LONG).show();
            Intent i = new Intent(ItemRegistrationActivity.this, ItemListActivity.class);
            ItemRegistrationActivity.this.startActivity(i);
            showProgress(false);
        }
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        addItemView.setVisibility(show ? View.GONE : View.VISIBLE);
        addItemView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                addItemView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        progressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}

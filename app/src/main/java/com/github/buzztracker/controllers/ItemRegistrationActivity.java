package com.github.buzztracker.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.buzztracker.R;
import com.github.buzztracker.model.ItemCategory;
import com.github.buzztracker.model.Model;

/**
 * Allows items to be registered into inventory
 */
public class ItemRegistrationActivity extends AppCompatActivity {

    private Model model;

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

        model = Model.getInstance();

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
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, model.getItemCategoryValuesAsString());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        locationSpinner = findViewById(R.id.item_location);
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, model.getLocationNames());
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

        // Allows us to cancel registration request if a field is invalid
        View focusView = model.getFirstIllegalItemField(shortDescView, longDescView,
                valueView, this);

        if (focusView != null) {
            focusView.requestFocus();
        } else {
            showProgress(true);

            Editable viewText = shortDescView.getText();
            String shortDesc = viewText.toString();
            shortDesc = shortDesc.trim();

            viewText = longDescView.getText();
            String longDesc = viewText.toString();
            longDesc = longDesc.trim();

            viewText = valueView.getText();
            String value = viewText.toString();
            value = value.trim();

            viewText = commentView.getText();
            String comment = viewText.toString();
            comment = comment.trim();

            Object locationSpinnerSelectedItem = locationSpinner.getSelectedItem();
            String location = locationSpinnerSelectedItem.toString();

            ItemCategory category = (ItemCategory) categorySpinner.getSelectedItem();

            model.addNewItem(shortDesc, longDesc, Integer.parseInt(value), comment, location,
                    category);

            Toast toast = Toast.makeText(ItemRegistrationActivity.this,
                    "Item successfully added to inventory", Toast.LENGTH_LONG);
            toast.show();
            Intent i = new Intent(ItemRegistrationActivity.this,
                    ItemListActivity.class);
            ItemRegistrationActivity.this.startActivity(i);
            showProgress(false);
        }
    }

    private void showProgress(final boolean show) {
        addItemView.setVisibility(show ? View.GONE : View.VISIBLE);
        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}

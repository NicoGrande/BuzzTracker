package com.github.buzztracker.controllers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.github.buzztracker.R;
import com.github.buzztracker.model.LocationManager;
import com.github.buzztracker.model.Model;


public class RegistrationActivity extends AppCompatActivity {

    Model model;

    // UI references
    private EditText emailView;
    private EditText passwordView1;
    private EditText passwordView2;
    private EditText firstNameView;
    private EditText lastNameView;
    private EditText phoneNumberView;
    private EditText locationView;
    private EditText managerView;
    private Spinner userTypeSpinner;

    // Allows hiding registration screen to show loading UI
    private View registrationView;
    private View progressView;

    // Manages registration request

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Cancel button
        Button registerCancelButton = (Button) findViewById(R.id.button_cancel);
        registerCancelButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                RegistrationActivity.this.startActivity(i);
            }
        });

        // RegistrationActivity button
        Button registerCompleteButton = (Button) findViewById(R.id.register_button);
        registerCompleteButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                attemptRegister();
            }
        });

        // Set up User type spinner
        userTypeSpinner = (Spinner) findViewById(R.id.user_type);
        ArrayAdapter<CharSequence> userTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.user_types, android.R.layout.simple_spinner_item);
        userTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(userTypeAdapter);

        // Adjusts ability to edit user-specific fields when LE type is selected
        // TODO: Fix enabling/disabling location and manager fields
//        userTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//            if (pos == 1) {
//                enableField(locationView);
//                enableField(managerView);
//            } else {
//                disableField(locationView);
//                disableField(managerView);
//           }
//        }
//
//        public void onNothingSelected(AdapterView<?> parent) {}
//        });

        // UI fields
        emailView = findViewById(R.id.email);
        passwordView1 = findViewById(R.id.password1);
        passwordView2 = findViewById(R.id.password2);
        firstNameView = findViewById(R.id.first_name);
        lastNameView = findViewById(R.id.last_name);
        phoneNumberView = findViewById(R.id.phone_number);
        locationView = findViewById(R.id.location);
        managerView = findViewById(R.id.manager);

        registrationView = findViewById(R.id.register_form);
        progressView = findViewById(R.id.register_progress);

        model = Model.getInstance();
        model.updateContext(this);
    }

    // Disables a passed in EditText field from being edited
    private void disableField(EditText field) {
        field.setEnabled(false);
        field.setFocusable(false);
        field.setAlpha((float) .5);
    }

    // Enables a passed in EditText field to be edited
    private void enableField(EditText field) {
        field.setEnabled(true);
        field.setFocusable(true);
        field.setAlpha((float) 1);
    }

    private void attemptRegister() {
        // Reset errors
        emailView.setError(null);
        passwordView1.setError(null);
        passwordView2.setError(null);
        firstNameView.setError(null);
        lastNameView.setError(null);
        phoneNumberView.setError(null);
        locationView.setError(null);
        managerView.setError(null);

        View focusView = model.getFirstIllegalRegistrationField(emailView, passwordView1, passwordView2,
                firstNameView, lastNameView, phoneNumberView, locationView, managerView, userTypeSpinner);

        if (focusView != null) {
            // There was an error or invalid field; cancel registration attempt and focus
            // first form field with an error
            focusView.requestFocus();
        } else {
            String password = passwordView1.getText().toString();
            String firstName = firstNameView.getText().toString().trim();
            String lastName = lastNameView.getText().toString().trim();
            String email = emailView.getText().toString().trim();
            String phoneNumber = phoneNumberView.getText().toString().trim();
            String userType = userTypeSpinner.getSelectedItem().toString();
            String location = locationView.getText().toString();
            // Show a progress spinner, and create user; advance to main screen
            showProgress(true);
            model.addNewUser(password, firstName, lastName, email, Long.parseLong(phoneNumber),
                    userType, LocationManager.getLocationFromName(location));
        }
    }

    /**
     * Toggles display between registration screen and a loading progress spinner
     *
     * @param show whether you want the loading progress spinner displayed
     */
    public void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        registrationView.setVisibility(show ? View.GONE : View.VISIBLE);
        registrationView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                registrationView.setVisibility(show ? View.GONE : View.VISIBLE);
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

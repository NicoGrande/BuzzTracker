package com.github.buzztracker.controllers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.buzztracker.FirebaseConstants;
import com.github.buzztracker.R;
import com.github.buzztracker.model.Admin;
import com.github.buzztracker.model.Location;
import com.github.buzztracker.model.LocationEmployee;
import com.github.buzztracker.model.LocationManager;
import com.github.buzztracker.model.Manager;
import com.github.buzztracker.model.Model;
import com.github.buzztracker.model.User;
import com.github.buzztracker.model.Verification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class RegistrationActivity extends AppCompatActivity {

    Model model;

    // UI references
    private EditText mEmailView;
    private EditText mPasswordView1;
    private EditText mPasswordView2;
    private EditText firstNameView;
    private EditText lastNameView;
    private EditText phoneNumberView;
    private EditText locationView;
    private EditText managerView;
    private Spinner usertypeSpinner;

    // Allows hiding registration screen to show loading UI
    private View mRegistrationView;
    private View mProgressView;

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

        // Set up Usertype spinner
        usertypeSpinner = (Spinner) findViewById(R.id.user_type);
        ArrayAdapter<CharSequence> usertypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.user_types, android.R.layout.simple_spinner_item);
        usertypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        usertypeSpinner.setAdapter(usertypeAdapter);

        // Adjusts ability to edit user-specific fields when LE type is selected
        // TODO: Fix enabling/disabling location and manager fields
//        usertypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
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
        mEmailView = findViewById(R.id.email);
        mPasswordView1 = findViewById(R.id.password1);
        mPasswordView2 = findViewById(R.id.password2);
        firstNameView = findViewById(R.id.first_name);
        lastNameView = findViewById(R.id.last_name);
        phoneNumberView = findViewById(R.id.phone_number);
        locationView = findViewById(R.id.location);
        managerView = findViewById(R.id.manager);

        mRegistrationView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);

        model = Model.getInstance();
        model.updateModel(this);
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
        mEmailView.setError(null);
        mPasswordView1.setError(null);
        mPasswordView2.setError(null);
        firstNameView.setError(null);
        lastNameView.setError(null);
        phoneNumberView.setError(null);
        locationView.setError(null);
        managerView.setError(null);

        View focusView = model.getFirstIllegalRegistrationField(mEmailView, mPasswordView1, mPasswordView2,
                firstNameView, lastNameView, phoneNumberView, locationView, managerView, usertypeSpinner);

        if (focusView != null) {
            // There was an error or invalid field; cancel registration attempt and focus
            // first form field with an error
            focusView.requestFocus();
        } else {
            String password = mPasswordView1.getText().toString();
            String firstName = firstNameView.getText().toString().trim();
            String lastName = lastNameView.getText().toString().trim();
            String email = mEmailView.getText().toString().trim();
            String phoneNumber = phoneNumberView.getText().toString().trim();
            String userType = usertypeSpinner.getSelectedItem().toString();
            String location = locationView.getText().toString();
            // Show a progress spinner, and create user; advance to main screen
            showProgress(true);
            model.addNewUser(password, firstName, lastName, email, Long.parseLong(phoneNumber),
                    userType, LocationManager.getLocationFromName(location));
        }
    }

    // Displays a loading screen circle
    public void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mRegistrationView.setVisibility(show ? View.GONE : View.VISIBLE);
        mRegistrationView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mRegistrationView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}

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
import com.github.buzztracker.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class RegistrationActivity extends AppCompatActivity {

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
    private UserRegisterTask mReg;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference().child(FirebaseConstants.FIREBASE_CHILD_USERS);

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

        String email = mEmailView.getText().toString().trim();
        String password1 = mPasswordView1.getText().toString().trim();
        String password2 = mPasswordView2.getText().toString().trim();
        String firstName = firstNameView.getText().toString().trim();
        String lastName = lastNameView.getText().toString().trim();
        String phoneNumber = parsePhoneNumber(phoneNumberView.getText().toString().trim());
        String location = locationView.getText().toString().trim();
        String managerName = managerView.getText().toString().trim();
        String userType = usertypeSpinner.getSelectedItem().toString().trim();

        // Allows us to cancel registration request if a field is invalid
        boolean cancel = false;
        View focusView = null;

        // Manager verification
        // Todo: Add manager verification
        if (userType.equals("Location Employee") && TextUtils.isEmpty(managerName)) {
            managerView.setError(getString(R.string.error_field_required));
            focusView = managerView;
            cancel = true;
        }

        // Location verification
        // Todo: Add location verification
        if (userType.equals("Location Employee") && TextUtils.isEmpty(location)) {
            locationView.setError(getString(R.string.error_field_required));
            focusView = locationView;
            cancel = true;
        }

        // Password verification
        if (!(password1.equals(password2))) {
            mPasswordView2.setError(getString(R.string.error_password_must_match));
            focusView = mPasswordView2;
            cancel = true;
        } else if (TextUtils.isEmpty(password2)) {
            mPasswordView2.setError(getString(R.string.error_field_required));
            focusView = mPasswordView2;
            cancel = true;
        }
        if (TextUtils.isEmpty(password1)) {
            mPasswordView1.setError(getString(R.string.error_field_required));
            focusView = mPasswordView1;
            cancel = true;
        } else if (!(isStrongPassword(password1))) {
            mPasswordView1.setError(getString(R.string.error_password_strength));
            focusView = mPasswordView1;
            cancel = true;
        }
      
        // Phone number verification
        if (TextUtils.isEmpty(phoneNumber)) {
            phoneNumberView.setError(getString(R.string.error_field_required));
            focusView = phoneNumberView;
            cancel = true;
            // Regex match for only numbers
        } else {
            if (!(isPhoneValid(phoneNumber))) {
                phoneNumberView.setError(getString(R.string.error_invalid_phone_number));
                focusView = phoneNumberView;
                cancel = true;
            }
        }

        // Email verification
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!(isPotentialEmail(email))) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Last name verification
        if (TextUtils.isEmpty(lastName)) {
            lastNameView.setError(getString(R.string.error_field_required));
            focusView = lastNameView;
            cancel = true;
        } else if (!(isNameLegal(lastName))) {
            lastNameView.setError(getString(R.string.error_invalid_name));
            focusView = lastNameView;
            cancel = true;
        }

        // Last name verification
        if (TextUtils.isEmpty(firstName)) {
            firstNameView.setError(getString(R.string.error_field_required));
            focusView = firstNameView;
            cancel = true;
        } else if (!(isNameLegal(firstName))) {
            firstNameView.setError(getString(R.string.error_invalid_name));
            focusView = firstNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error or invalid field; cancel registration attempt and focus
            // first form field with an error
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and create user; advance to main screen
            showProgress(true);
            mReg = new UserRegisterTask(password1, firstName, lastName, email, Long.parseLong(phoneNumber),
                    userType, LocationManager.getLocationFromName(location));
            mReg.execute((Void) null);
        }
    }

    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private String pw;
        private String fName;
        private String lName;
        private String email;
        private Long phoneNum;
        private String userType;
        private Location location;


        UserRegisterTask(String pw, String fName, String lName, String email, Long phoneNum,
                         String userType, Location location) {
            this.pw = pw;
            this.fName = fName;
            this.lName = lName;
            this.email = email;
            this.phoneNum = phoneNum;
            this.userType = userType;
            this.location = location;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            // keeps track of registration progress
            final AtomicBoolean success = new AtomicBoolean(false);
            final AtomicBoolean finishedAttempt = new AtomicBoolean(false);

            try {
                mAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String userId = mAuth.getCurrentUser().getUid();
                                    createUser(pw, fName, lName, email, phoneNum, userType,
                                            location);
                                    success.set(true);
                                    finishedAttempt.set(true);
                                } else {
                                    finishedAttempt.set(true);
                                }
                            }
                        });
                // waits 10 seconds to try to register with FirebaseAuth, fails if unable
                for (int i = 0; i < 10; i++) {
                    if (finishedAttempt.get()) {
                        return success.get();
                    }
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                return false;
            }

            return success.get();
        }

        @Override
        protected void onPostExecute(final Boolean taskSuccess) {
            mReg = null;
            showProgress(false);

            if (taskSuccess) {
                Toast.makeText(getApplicationContext(), "Account successfully created", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(RegistrationActivity.this, MainScreenActivity.class);
                RegistrationActivity.this.startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "User already exists with this email",
                        Toast.LENGTH_LONG).show();
            }
            Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
            RegistrationActivity.this.startActivity(i);
        }

        @Override
        protected void onCancelled() {
            mReg = null;
            showProgress(false);
        }
    }

    private void createUser(String pw, String fName, String lName, String email, Long phoneNum,
                            String userType, Location loc) {
        User newUser;
        if (userType.equals("User")) {
            newUser = new User(pw, fName, lName, email, phoneNum);
        } else if (userType.equals("Location Employee")) {
            newUser = new LocationEmployee(pw, fName, lName, email, phoneNum, loc);
        } else if (userType.equals("Admin")) {
            newUser = new Admin(pw, fName, lName, email, phoneNum);
        } else {
            newUser = new Manager(pw, fName, lName, email, phoneNum);
        }

        saveUserToFirebase(newUser, userType);
    }

    private void saveUserToFirebase(User user, String userType) {
        mDatabaseRef = mDatabaseRef.child(userType);
        mDatabaseRef.push().setValue(user);

    }

    // Removes -, ', and whitespace from names
    private boolean isNameLegal(String name) {
        name = name.replaceAll("\\s+", "");
        name = name.replaceAll("-", "");
        name = name.replaceAll("\\'", "");
        // Matches all international characters
        return name.matches("^[\\p{L}]+$");
    }

    // Verify strength of password
    // Requires at least:
    //     8 characters
    //     One uppercase letter
    //     One lowercase letter
    //     One number
    private boolean isStrongPassword(String password) {
        if (password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[a-z].*")
                && password.matches(".*\\d+.*")) {
            return true;
        }
        return false;
    }

    // Verifies possible email addresses with complicated regex (SO to StackOverflow)
    private boolean isPotentialEmail(String email) {
        if (email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+" +
                ")*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\" +
                "[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+" +
                "[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?" +
                "[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*" +
                "[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\" +
                "[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
            return true;
        }
        return false;
    }

    // Determines if parsed phone number has only numbers and is not empty
    private boolean isPhoneValid(String number) {
        return number.matches("\\d+");
    }

    // Pulls only numbers out of phone number; removes (), ., -, +, and white space
    private String parsePhoneNumber(String number) {
        String[] nums = number.split("(\\s|\\(|\\)|-|\\.|\\+)*");
        number = "";
        for (String numbers: nums) {
            number += numbers;
        }
        return number;
    }

    // Displays a loading screen circle
    private void showProgress(final boolean show) {
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

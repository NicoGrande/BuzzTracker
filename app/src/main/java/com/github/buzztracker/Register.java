package com.github.buzztracker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Register extends AppCompatActivity {

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
    private UserRegisterTask mRegisterTask = null;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        // Cancel button
        Button registerCancelButton = (Button) findViewById(R.id.button_cancel);
        registerCancelButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent i = new Intent(Register.this, Login.class);
                Register.this.startActivity(i);
            }
        });

        // Register button
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

        final String email = mEmailView.getText().toString().trim();
        final String password1 = mPasswordView1.getText().toString().trim();
        String password2 = mPasswordView2.getText().toString().trim();
        final String firstname = firstNameView.getText().toString().trim();
        final String lastname = lastNameView.getText().toString().trim();
        final String phonenumber = parsePhoneNumber(phoneNumberView.getText().toString().trim());
        String location = locationView.getText().toString().trim();
        String managername = managerView.getText().toString().trim();
        String userType = usertypeSpinner.getSelectedItem().toString().trim();

        // Allows us to cancel registration request if a field is invalid
        boolean cancel = false;
        View focusView = null;

        // Manager verification
        // Todo: Add manager verification
        if (userType.equals("Location Employee") && TextUtils.isEmpty(managername)) {
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
        if (TextUtils.isEmpty(phonenumber)) {
            phoneNumberView.setError(getString(R.string.error_field_required));
            focusView = phoneNumberView;
            cancel = true;
            // Regex match for only numbers
        } else {
            if (!(isPhoneValid(phonenumber))) {
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
        if (TextUtils.isEmpty(lastname)) {
            lastNameView.setError(getString(R.string.error_field_required));
            focusView = lastNameView;
            cancel = true;
        } else if (!(isNameLegal(lastname))) {
            lastNameView.setError(getString(R.string.error_invalid_name));
            focusView = lastNameView;
            cancel = true;
        }

        // Last name verification
        if (TextUtils.isEmpty(firstname)) {
            firstNameView.setError(getString(R.string.error_field_required));
            focusView = firstNameView;
            cancel = true;
        } else if (!(isNameLegal(firstname))) {
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


            mAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        String userId = mAuth.getCurrentUser().getUid();

                        User user = new User(password1, firstname, lastname, email, Long.parseLong(phonenumber));

                        mDatabase.child("users").child(userId).setValue(user);


                        Intent myIntent = new Intent(Register.this, MainScreenActivity.class);
                        Register.this.startActivity(myIntent);
                        showProgress(false);


                    } else {

                        Toast.makeText(Register.this, "Account already exists with this Email.", Toast.LENGTH_LONG).show();
                        Intent myIntent = new Intent(Register.this, Login.class);
                        Register.this.startActivity(myIntent);
                        showProgress(false);
                    }

                }
            });

        }
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

    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String mFirstname;
        private final String mLastname;
        private final String mPhoneNumber;
        private final String mLocation;
        private final String mManager;
        private final String mUsertype;

        UserRegisterTask(String email, String password, String firstname, String lastname,
                         String phonenumber, String location, String manager, String usertype) {
            mEmail = email.trim();
            mPassword = password.trim();
            mFirstname = firstname.trim();
            mLastname = lastname.trim();
            mPhoneNumber = phonenumber.trim();
            mLocation = location.trim();
            mManager = manager.trim();
            mUsertype = usertype.trim();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            // TODO: register the new account here. Verify unique email
            if (User.credentials.containsKey(mEmail)) {
                return false;
            } else {
                mAuth.createUserWithEmailAndPassword(mEmail, mPassword);
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mRegisterTask = null;
            showProgress(false);
            if (success) {
                Intent myIntent = new Intent(Register.this, MainScreenActivity.class);
                Register.this.startActivity(myIntent);
                ;
            } else {
                mEmailView.setError(getString(R.string.email_already_used));
                mEmailView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mRegisterTask = null;
            showProgress(false);
        }
    }
}

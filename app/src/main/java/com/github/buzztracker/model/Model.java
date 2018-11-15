package com.github.buzztracker.model;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.buzztracker.FirebaseConstants;
import com.github.buzztracker.R;
import com.github.buzztracker.controllers.LoginActivity;
import com.github.buzztracker.controllers.MainScreenActivity;
import com.github.buzztracker.controllers.RegistrationActivity;
import com.github.buzztracker.controllers.SearchListActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Model {

    private final static Model instance = new Model();
    public static Model getInstance() {
        return instance;
    }

    private UserLoginTask userLoginTask;
    private UserRegisterTask userRegisterTask;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase databaseInstance;
    private DatabaseReference databaseReference;
    private List<Item> inventory;
    private List<Location> locations;

    private Model() {

        initializeModel();
        updateInventory();
    }

    // Sets up the model; only throws the exception in JUnit test states
    private void initializeModel() {
        try {
            databaseInstance = FirebaseDatabase.getInstance();
            databaseReference = databaseInstance.getReference();
            firebaseAuth = FirebaseAuth.getInstance();

            getInitialItemId();
            populateInventory();
        } catch (ExceptionInInitializerError error) {
            Log.e("Model initialization", error.getMessage());
        }
    }

    public View getFirstIllegalLoginField(AutoCompleteTextView emailView, EditText passwordView, Context context) {
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, R.string.error_field_required,
                    Toast.LENGTH_SHORT).show();
            focusView = passwordView;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(context, R.string.error_field_required,
                    Toast.LENGTH_SHORT).show();
            focusView = emailView;
        } else if (!Verification.isPotentialEmail(email)) {
            Toast.makeText(context, R.string.error_invalid_email,
                    Toast.LENGTH_SHORT).show();
            focusView = emailView;
        }
        return focusView;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void verifyLogin(String email, String password, Context context) {
        if (userLoginTask != null) {
            return;
        }

        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        ((LoginActivity) context).showProgress(true);
        userLoginTask = new UserLoginTask(email, password, context);
        userLoginTask.execute((Void) null);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String email;
        private final String password;
        private final Context context;

        UserLoginTask(String email, String password, Context context) {
            this.email = email;
            this.password = password;
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: add alternative network logins

            // keeps track of Firebase authentication success
            // is kinda weird but AtomicBoolean allows it to be modified from with lambda function
            final AtomicBoolean success = new AtomicBoolean(false);
            final AtomicBoolean finishedAttempt = new AtomicBoolean(false);
            try {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            success.set(true);
                            finishedAttempt.set(true);
                        } else {
                            finishedAttempt.set(true);
                        }
                    }
                });

                // waits 10 seconds to try to login with FirebaseAuth, fails if unable
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
        protected void onPostExecute(final Boolean success) {
            userLoginTask = null;
            ((LoginActivity) context).showProgress(false);

            if (success) {
                Toast.makeText(context, R.string.login_success, Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(context, MainScreenActivity.class);
                context.startActivity(myIntent);
            } else {
                Toast.makeText(context, R.string.error_incorrect_password,
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            userLoginTask = null;
            ((LoginActivity) context).showProgress(false);
        }
    }

    public void populateInventory() {
        Inventory.clear();
        databaseReference = databaseInstance.getReference()
                .child(FirebaseConstants.FIREBASE_CHILD_ITEMS);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Item item = postSnapshot.getValue(Item.class);
                    Inventory.addToInventory(item);
                    Log.d("Item loaded in: ", item != null ? item.getShortDesc() : null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Item read failed: ", databaseError.getMessage());
            }
        });
    }

    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private String password;
        private String firstName;
        private String lastName;
        private String email;
        private Long phoneNum;
        private String userType;
        private Location location;
        private Context context;


        UserRegisterTask(String password, String firstName, String lastName, String email,
                         Long phoneNum, String userType, Location location, Context context) {
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.phoneNum = phoneNum;
            this.userType = userType;
            this.location = location;
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            // keeps track of registration progress
            final AtomicBoolean success = new AtomicBoolean(false);
            final AtomicBoolean finishedAttempt = new AtomicBoolean(false);

            try {
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String userId = firebaseAuth.getCurrentUser().getUid();
                                    User newUser = createUser(password, firstName, lastName, email,
                                            phoneNum, userType, location);
                                    saveUserToFirebase(newUser, userType);
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
            userRegisterTask = null;
            ((RegistrationActivity) context).showProgress(false);

            if (taskSuccess) {
                Toast.makeText(context, R.string.account_creation_success,
                        Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, MainScreenActivity.class);
                context.startActivity(i);
            } else {
                Toast.makeText(context, R.string.error_user_already_exists,
                        Toast.LENGTH_LONG).show();
            }
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
        }

        @Override
        protected void onCancelled() {
            userRegisterTask = null;
            ((RegistrationActivity) context).showProgress(false);
        }
    }

    public View getFirstIllegalRegistrationField(
            EditText emailView, EditText passwordView1, EditText passwordView2,
            EditText firstNameView, EditText lastNameView, EditText phoneNumView,
            EditText locationView, EditText managerView, Spinner userTypeSpinner, Context context) {
        String email = emailView.getText().toString().trim();
        String password1 = passwordView1.getText().toString().trim();
        String password2 = passwordView2.getText().toString().trim();
        String firstName = firstNameView.getText().toString().trim();
        String lastName = lastNameView.getText().toString().trim();
        String phoneNumber = Verification.parsePhoneNumber(
                phoneNumView.getText().toString().trim());
        phoneNumber = Verification.removeCommonNameChars(phoneNumber);
        String location = locationView.getText().toString().trim();
        String managerName = managerView.getText().toString().trim();
        String userType = userTypeSpinner.getSelectedItem().toString().trim();
        View focusView = null;

        // Manager verification
        // Todo: Add manager verification
        if (userType.equals("Location Employee") && TextUtils.isEmpty(managerName)) {
            managerView.setError(context.getString(R.string.error_field_required));
            focusView = managerView;
        }

        // Location verification
        // Todo: Add location verification
        if (userType.equals("Location Employee") && TextUtils.isEmpty(location)) {
            locationView.setError(context.getString(R.string.error_field_required));
            focusView = locationView;
        }

        // Password verification
        if (!(password1.equals(password2))) {
            passwordView2.setError(context.getString(R.string.error_password_must_match));
            focusView = passwordView2;
        } else if (TextUtils.isEmpty(password2)) {
            passwordView2.setError(context.getString(R.string.error_field_required));
            focusView = passwordView2;
        }
        if (TextUtils.isEmpty(password1)) {
            passwordView1.setError(context.getString(R.string.error_field_required));
            focusView = passwordView1;
        } else if (!(Verification.isStrongPassword(password1))) {
            passwordView1.setError(context.getString(R.string.error_password_strength));
            focusView = passwordView1;
        }

        // Phone number verification
        if (TextUtils.isEmpty(phoneNumber)) {
            phoneNumView.setError(context.getString(R.string.error_field_required));
            focusView = phoneNumView;
        } else {
            if (!(Verification.isPhoneValid(phoneNumber))) {
                phoneNumView.setError(context.getString(
                        R.string.error_invalid_phone_number));
                focusView = phoneNumView;
            }
        }

        // Email verification
        if (TextUtils.isEmpty(email)) {
            emailView.setError(context.getString(R.string.error_field_required));
            focusView = emailView;
        } else if (!(Verification.isPotentialEmail(email))) {
            emailView.setError(context.getString(R.string.error_invalid_email));
            focusView = emailView;
        }

        // Last name verification
        if (TextUtils.isEmpty(lastName)) {
            lastNameView.setError(context.getString(R.string.error_field_required));
            focusView = lastNameView;
        } else if (!(Verification.isNameLegal(lastName))) {
            lastNameView.setError(context.getString(R.string.error_invalid_name));
            focusView = lastNameView;
        }

        // Last name verification
        if (TextUtils.isEmpty(firstName)) {
            firstNameView.setError(context.getString(R.string.error_field_required));
            focusView = firstNameView;
        } else if (!(Verification.isNameLegal(firstName))) {
            firstNameView.setError(context.getString(R.string.error_invalid_name));
            focusView = firstNameView;
        }
        return focusView;
    }

    public void addNewUser(String password, String firstName, String lastName, String email,
                           Long phoneNum, String userType, Location location, Context context) {
        if (userRegisterTask != null) {
            return;
        }

        // Show a progress spinner and kick off a background task to
        // perform the user registration attempt
        ((RegistrationActivity) context).showProgress(true);
        userRegisterTask = new UserRegisterTask(password, firstName, lastName, email, phoneNum,
                userType, location, context);
        userRegisterTask.execute((Void) null);
    }

    public User createUser(String pw, String fName, String lName, String email, Long phoneNum,
                            String userType, Location loc) {
        User newUser;
        if (userType.equals("User")) {
            newUser = new User(pw, fName, lName, email, phoneNum);
        } else if (userType.equals("Location Employee")) {
            newUser = new LocationEmployee(pw, fName, lName, email, phoneNum, loc);
        } else if (userType.equals("Admin")) {
            newUser = new Admin(pw, fName, lName, email, phoneNum);
        } else if (userType.equals("Manager")){
            newUser = new Manager(pw, fName, lName, email, phoneNum);
        } else {
            throw new IllegalArgumentException("Invalid user type");
        }

        return newUser;
    }

    private void saveUserToFirebase(User user, String userType) {
        databaseReference = databaseInstance
                .getReference()
                .child(FirebaseConstants.FIREBASE_CHILD_USERS)
                .child(userType);
        databaseReference.push().setValue(user);
    }

    public void updateInventory() {
        inventory = Inventory.getInventory();
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void updateLocations(Context context) {
        InputStream locationsInfo = context.getResources().openRawResource(R.raw.locations);
        CSVReader.parseCSV(locationsInfo);
        locations = LocationManager.getLocations();
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void searchByKeyword(String searchKeywords, String location, Context context) {
        String[] keywords = searchKeywords.split("\\s+");
        String[] itemDescriptionWords;
        List<Item> filteredItems = new ArrayList<>();

        if (location.equals("Search all locations")) {
            for (Item item : inventory) {
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
            for (Item item : inventory) {
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
        displayResults(filteredItems, context);
    }

    public void searchByCategory(ItemCategory category, String location, Context context) {
        List<Item> filteredItems = new ArrayList<>();

        if (location.equals("Search all locations")) {
            for (Item item : inventory) {
                // if searching all categories
                if (category == null) {
                    filteredItems.add(item);
                } else if (item.getCategory() == category) {
                    filteredItems.add(item);
                }
            }
        } else {
            for (Item item : inventory) {
                if (item.getLocation().getLocationName().equals(location)) {
                    if (category == null) {
                        filteredItems.add(item);
                    } else if (item.getCategory() == category) {
                        filteredItems.add(item);
                    }
                }
            }
        }
        displayResults(filteredItems, context);
    }

    public String[] getDescriptionKeywords(Item item) {
        String description = item.getFullDesc() + " " + item.getShortDesc();
        return description.split("\\s+");
    }

    public ItemCategory getCategory(String cat) {
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

    private void displayResults(List<Item> items, Context context) {
        if (items.size() == 0) {
            Toast.makeText(context, R.string.search_no_matches, Toast.LENGTH_LONG).show();
        } else {
            Inventory.setFilteredInventory(items);
            Intent i = new Intent(context, SearchListActivity.class);
            context.startActivity(i);
        }
    }

    public View getFirstIllegalItemField(EditText shortDescView, EditText longDescView,
                                         EditText valueView, Context context) {

        String shortDesc = shortDescView.getText().toString().trim();
        String longDesc = longDescView.getText().toString().trim();
        String value = valueView.getText().toString().trim();
        View focusView = null;
        // Value verification
        if (TextUtils.isEmpty(value)) {
            valueView.setError(context.getString(R.string.error_field_required));
            focusView = valueView;
        } else {
            int i;
            try {
                i = Integer.parseInt(value);
                if (i < 0) {
                    valueView.setError(context.getString(R.string.error_invalid_cost));
                    focusView = valueView;
                }
            } catch (NumberFormatException e) {
                valueView.setError(context.getString(R.string.error_invalid_cost));
                focusView = valueView;
            }
        }

        // Long description verification
        if (TextUtils.isEmpty(longDesc)) {
            longDescView.setError(context.getString(R.string.error_field_required));
            focusView = longDescView;
        }

        // Short description verification
        if (TextUtils.isEmpty(shortDesc)) {
            shortDescView.setError(context.getString(R.string.error_field_required));
            focusView = shortDescView;
        }
        return focusView;
    }

    public void addNewItem(String shortDesc, String longDesc, int value, String comment,
                           String location, ItemCategory category) {
        Location loc = LocationManager.getLocationFromName(location);
        Item item;
        if (comment.isEmpty()) {
            item = new Item(loc, shortDesc, longDesc, value, category);
        } else {
            item = new Item(loc, shortDesc, longDesc, value, category, comment);
        }
        Inventory.addToInventory(item);
        saveItemToFirebase(item);
        updateInventory();
        saveIDToFirebase(item.getId());
    }

    private void saveItemToFirebase(Item item) {
        databaseReference = databaseInstance.getReference()
                .child(FirebaseConstants.FIREBASE_CHILD_ITEMS);
        databaseReference.push().setValue(item);
    }

    private void getInitialItemId() {
        databaseReference = databaseInstance.getReference()
                .child(FirebaseConstants.FIREBASE_CHILD_ITEM_COUNTER);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Item.idCounter = (int) ((long) dataSnapshot.getValue());
                Log.d("Last item ID loaded: ", "" + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Item ID read failed: ", databaseError.getMessage());
            }
        });
    }

    private void saveIDToFirebase(int id) {
        databaseReference = databaseInstance.getReference()
                .child(FirebaseConstants.FIREBASE_CHILD_ITEM_COUNTER);
        databaseReference.setValue(id);
    }

    public List<Item> getFilteredInventory() {
        return Inventory.getFilteredInventory();
    }
}

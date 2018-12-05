package com.github.buzztracker.model;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.buzztracker.R;
import com.github.buzztracker.controllers.SearchListActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.buzztracker.model.Constants.*;

/**
 * Singleton instance that basically does everything
 */
public final class Model {

    private static final Model instance = new Model();

    /**
     * Provides a means of acquiring the singleton in other classes
     *
     * @return an instance of Model
     */
    public static Model getInstance() {
        return instance;
    }

    private FirebaseDatabase databaseInstance;
    private DatabaseReference databaseReference;
    private final InventoryManager inventoryManager;
    private List<Item> inventory;
    private final LocationManager locationManager;
    private List<Location> locations;
    private final Login loginAttempt;
    private static AtomicInteger loginComplete;
    private static AtomicInteger registerComplete;

    private Model() {
        locationManager = LocationManager.getInstance();
        inventoryManager = InventoryManager.getInstance();
        Model.initializeCompletes();
        loginAttempt = new Login();
        initializeModel();
        updateInventory();
    }

    // Sets up the model; only throws the exception in JUnit test states
    private void initializeModel() {
        try {
            databaseInstance = FirebaseDatabase.getInstance();
            databaseReference = databaseInstance.getReference();

            getInitialItemId();
            populateInventory();
        } catch (ExceptionInInitializerError ignored) {}
    }

    private static void initializeCompletes() {
        loginComplete = new AtomicInteger(NOT_STARTED);
        registerComplete = new AtomicInteger(NOT_STARTED);
    }

    /**
     * Determines if a View holds valid information for logging in
     *
     * @param emailView View containing email information
     * @param passwordView View containing password information
     * @param context application context
     * @return the topmost invalid View if there is an issue; null otherwise
     */
    public View getFirstIllegalLoginField(AutoCompleteTextView emailView,
                                          EditText passwordView, Context context) {
        return loginAttempt.getFirstIllegalLoginField(emailView, passwordView, context);
    }

    /**
     * Attempts to login with Firebase with given login info
     *
     * @param email email address
     * @param password password associated with email
     * @param context application context
     */
    public void verifyLogin(String email, String password, Context context) {
        if (loginComplete.get() != NOT_STARTED) {
            return;
        }
        setLoginComplete(IN_PROGRESS);

        loginAttempt.verifyLogin(email, password, context);
    }

    private void populateInventory() {
        inventoryManager.clear();
        databaseReference = databaseInstance.getReference();
        databaseReference = databaseReference.child(Constants.FIREBASE_CHILD_ITEMS);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Item item = postSnapshot.getValue(Item.class);
                    inventoryManager.addToInventory(item);
                    Log.d("Item loaded in: ", (item != null) ? item.getShortDesc() : null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Item read failed: ", databaseError.getMessage());
            }
        });
    }

    /**
     * Determines if any user registration fields are invalid
     *
     * @param emailView View containing email
     * @param passwordView1 View containing password
     * @param passwordView2 View containing the password re-entered
     * @param firstNameView View containing the first name
     * @param lastNameView View containing the last name
     * @param phoneNumView View containing the phone number
     * @param locationView View containing the location (optional)
     * @param managerView View containing the manager (optional)
     * @param userTypeSpinner View containing the type of user
     * @param context application context
     * @return the topmost View that has an error if there is one; null otherwise
     */
    public View getFirstIllegalRegistrationField(
            EditText emailView, EditText passwordView1, EditText passwordView2,
            EditText firstNameView, EditText lastNameView, EditText phoneNumView,
            EditText locationView, EditText managerView, Spinner userTypeSpinner, Context context) {



        Editable viewText = passwordView1.getText();
        String password1 = viewText.toString();
        password1 = password1.trim();

        viewText = passwordView2.getText();
        String password2 = viewText.toString();
        password2 = password2.trim();

        viewText = phoneNumView.getText();
        String phoneNumber = viewText.toString();
        phoneNumber = phoneNumber.trim();
        phoneNumber = getParsedPhoneNumber(phoneNumber);

        viewText = locationView.getText();
        String location = viewText.toString();
        location = location.trim();

        viewText = managerView.getText();
        String managerName = viewText.toString();
        managerName = managerName.trim();

        Object spinnerSelectedItem = userTypeSpinner.getSelectedItem();
        String userType = spinnerSelectedItem.toString();
        userType = userType.trim();

        View focusView = null;
        View tempView;

        // Manager verification
        if ("Location Employee".equals(userType) && TextUtils.isEmpty(managerName)) {
            managerView.setError(context.getString(R.string.error_field_required));
            focusView = managerView;
        }

        // Location verification
        if ("Location Employee".equals(userType) && TextUtils.isEmpty(location)) {
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
        } else if (!(isPhoneValid(phoneNumber))) {
                phoneNumView.setError(context.getString(
                        R.string.error_invalid_phone_number));
                focusView = phoneNumView;
        }

        tempView = checkEmailForError(emailView, context);
        if (tempView != null) {
            focusView = tempView;
        }
        tempView = checkNameForError(lastNameView, context);
        if (tempView != null) {
            focusView = tempView;
        }
        tempView = checkNameForError(firstNameView, context);
        if (tempView != null) {
            focusView = tempView;
        }
        return focusView;
    }

    private View checkEmailForError(EditText view, Context context) {
        Editable viewText = view.getText();
        String email = viewText.toString();
        email = email.trim();
        boolean hasIssue = false;

        if (TextUtils.isEmpty(email)) {
            view.setError(context.getString(R.string.error_field_required));
            hasIssue = true;
        } else if (!(Verification.isPotentialEmail(email))) {
            view.setError(context.getString(R.string.error_invalid_email));
            hasIssue = true;
        }
        return hasIssue ? view : null;
    }

    private View checkNameForError(EditText view, Context context) {
        Editable viewText = view.getText();
        String text = viewText.toString();
        text = text.trim();
        boolean hasIssue = false;

        if (TextUtils.isEmpty(text)) {
            view.setError(context.getString(R.string.error_field_required));
            hasIssue = true;
        } else if (!(isNameLegal(text))) {
            view.setError(context.getString(R.string.error_invalid_name));
            hasIssue = true;
        }
        return hasIssue ? view : null;
    }

    private boolean isNameLegal(String name) {
        return Verification.isNameLegal(name);
    }

    private String getParsedPhoneNumber(String phoneNumber) {
        String newNumber = Verification.parsePhoneNumber(phoneNumber);
        newNumber = Verification.removeCommonNameChars(newNumber);
        return newNumber;
    }

    private boolean isPhoneValid(String phoneNumber) {
        return Verification.isPhoneValid(phoneNumber);
    }

    /**
     * Attempts to register new user with Firebase
     *
     * @param password password
     * @param firstName first name
     * @param lastName last name
     * @param email email address
     * @param phoneNum phone number
     * @param userType type of user
     * @param location location
     * @param context application context
     */
    public void addNewUser(String password, String firstName, String lastName, String email,
                           Long phoneNum, String userType, Location location, Context context) {
        if (registerComplete.get() != NOT_STARTED) {
            return;
        }
        setRegisterComplete(IN_PROGRESS);

        // Show a progress spinner and kick off a background task to
        // perform the user registration attempt
        User newUser = createUser(password, firstName, lastName, email,
                phoneNum, userType, location);
        UserRegisterTask userRegisterTask = new UserRegisterTask(newUser, userType, context);
        userRegisterTask.execute((Void) null);
    }

    /**
     * Creates an instance of User
     *
     * @param pw password
     * @param fName first name
     * @param lName last name
     * @param email email address
     * @param phoneNum phone number
     * @param userType type of user to be created
     * @param loc location
     * @return the created user
     */
    public User createUser(String pw, String fName, String lName, String email, Long phoneNum,
                            String userType, Location loc) {
        User newUser;
        if ("User".equals(userType)) {
            newUser = new User(pw, fName, lName, email, phoneNum);
        } else if ("Location Employee".equals(userType)) {
            Object[] userData = {pw, fName, lName, email, phoneNum, loc};
            newUser = new LocationEmployee(userData);
        } else if ("Admin".equals(userType)) {
            newUser = new Admin(pw, fName, lName, email, phoneNum);
        } else if ("Manager".equals(userType)){
            newUser = new Manager(pw, fName, lName, email, phoneNum);
        } else {
            throw new IllegalArgumentException("Invalid user type");
        }

        return newUser;
    }

    private void updateInventory() {
        inventory = inventoryManager.getInventory();
    }

    /**
     * Provides means for classes to get the list of inventory items
     *
     * @return list of items in inventory
     */
    public List<Item> getInventory() {
        return Collections.unmodifiableList(inventory);
    }

    /**
     * Reads in locations from CSV and adds them to location list
     *
     * @param context application context
     */
    public void updateLocations(Context context) {
        Resources resources = context.getResources();
        InputStream locationsInfo = resources.openRawResource(R.raw.locations);
        CSVReader csvReader = new CSVReader();
        csvReader.parseCSV(locationsInfo, locationManager);
        locations = locationManager.getLocations();
    }

    /**
     * Provides means for classes to get the list of locations
     *
     * @return a list containing all the locations
     */
    public List<Location> getLocations() {
        return Collections.unmodifiableList(locations);
    }

    /**
     * Searches inventory by keyword and generates a filtered list of items
     * that match the search criteria
     *
     * @param searchKeywords keywords being used for the search
     * @param location the location at which to search for the items
     * @param context application context
     */
    public void searchByKeyword(String searchKeywords, String location, Context context) {
        String[] keywords = searchKeywords.split("\\s+");
        String[] itemDescriptionWords;
        List<Item> filteredItems = new ArrayList<>();

        if ("Search all locations".equals(location)) {
            for (Item item : inventory) {
                itemDescriptionWords = getDescriptionKeywords(item);
                outerLoop:
                for (String keyword : keywords) {
                    for (String itemDescriptionWord : itemDescriptionWords) {
                        if (keyword.equals(itemDescriptionWord)) {
                            filteredItems.add(item);
                            break outerLoop;
                        }
                    }
                }
            }
        } else {
            for (Item item : inventory) {
                Location itemLocation = item.getLocation();
                String itemLocationName = itemLocation.getLocationName();
                if (itemLocationName.equals(location)) {
                    itemDescriptionWords = getDescriptionKeywords(item);
                    outerLoop:
                    for (String keyword : keywords) {
                        for (String itemDescriptionWord : itemDescriptionWords) {
                            if (keyword.equals(itemDescriptionWord)) {
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

    /**
     * Searches inventory by category and generates a filtered list of items
     * that match the search criteria
     *
     * @param category the category of items to search for
     * @param location the location at which to search for the items
     * @param context application context
     */
    public void searchByCategory(ItemCategory category, String location, Context context) {
        List<Item> filteredItems = new ArrayList<>();

        if ("Search all locations".equals(location)) {
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
                Location itemLocation = item.getLocation();
                String itemLocationName = itemLocation.getLocationName();
                if (itemLocationName.equals(location)) {
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

    private String[] getDescriptionKeywords(Item item) {
        String description = item.getFullDesc() + " " + item.getShortDesc();
        return description.split("\\s+");
    }

    /**
     * Gets the ItemCategory for a String of matching name
     *
     * @param cat the String matching the desired ItemCategory name
     * @return the ItemCategory that has a name matching cat
     */
    public ItemCategory getCategory(String cat) {
        if ("Search all categories".equals(cat)) {
            return null;
        }
        for (ItemCategory itemCategory : ItemCategory.values()) {
            String categoryString = itemCategory.getAsString();
            if (categoryString.equals(cat)) {
                return itemCategory;
            }
        }
        return null;
    }

    private void displayResults(List<Item> items, Context context) {
        if (items.isEmpty()) {
            Toast toast = Toast.makeText(context, R.string.search_no_matches, Toast.LENGTH_LONG);
            toast.show();
        } else {
            inventoryManager.setFilteredInventory(items);
            Intent i = new Intent(context, SearchListActivity.class);
            context.startActivity(i);
        }
    }

    /** Determines if the Item registration fields have invalid information
     *
     * @param shortDescView View containing a short description of the item
     * @param longDescView View containing a long description of the item
     * @param valueView View containing the value of the item
     * @param context application context
     * @return the topmost View that has an invalid field if there is one; null otherwise
     */
    public View getFirstIllegalItemField(EditText shortDescView, EditText longDescView,
                                         EditText valueView, Context context) {

        Editable viewText = shortDescView.getText();
        String shortDesc = viewText.toString();
        shortDesc = shortDesc.trim();

        viewText = longDescView.getText();
        String longDesc = viewText.toString();
        longDesc = longDesc.trim();

        viewText = valueView.getText();
        String value = viewText.toString();
        value = value.trim();

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

    /**
     * Adds a new item to the inventory
     *
     * @param shortDesc short description
     * @param longDesc long description
     * @param value cost in dollars
     * @param comment optional comment
     * @param location location
     * @param category category
     */
    public void addNewItem(String shortDesc, String longDesc, int value, String comment,
                           String location, ItemCategory category) {
        Location loc = locationManager.getLocationFromName(location);
        Item item;
        if (comment.isEmpty()) {
            Object[] itemData = {loc, shortDesc, longDesc, value, category};
            item = new Item(itemData);
        } else {
            Object[] itemData = {loc, shortDesc, longDesc, value, category};
            item = new Item(itemData, comment);
        }
        inventoryManager.addToInventory(item);
        saveItemToFirebase(item);
        updateInventory();
        saveIDToFirebase(item.getId());
    }

    private void saveItemToFirebase(Item item) {
        databaseReference = databaseInstance.getReference()
                .child(Constants.FIREBASE_CHILD_ITEMS);
        databaseReference.push().setValue(item);
    }

    private void getInitialItemId() {
        databaseReference = databaseInstance.getReference()
                .child(Constants.FIREBASE_CHILD_ITEM_COUNTER);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Item.setIdCounter((int) ((long) dataSnapshot.getValue()));
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
                .child(Constants.FIREBASE_CHILD_ITEM_COUNTER);
        databaseReference.setValue(id);
    }

    /**
     * Public getter for the filtered list created by searching
     *
     * @return a list containing the filtered search items matching the most recent search
     */
    public List<Item> getFilteredInventory() {
        return inventoryManager.getFilteredInventory();
    }

    /**
     * Getter for a list of the location coordinates for the map
     *
     * @return a list containing the LatLng coordinates of each location
     */
    public List<LatLng> getLocationCoords() {
        return locationManager.getLocationCoords();
    }

    /**
     * Getter for all ItemCategories as strings
     *
     * @return list of all ItemCategories as strings
     */
    public List<String> getItemCategoryValuesAsString() {
        ItemCategory[] categories = ItemCategory.values();
        List<String> stringCategories = new ArrayList<>();

        for (ItemCategory category : categories) {
            stringCategories.add(category.toString());
        }
        return stringCategories;
    }

    static synchronized void setLoginComplete(int status) {
        loginComplete.set(status);
    }

    static synchronized void setRegisterComplete(int status) {
        registerComplete.set(status);
    }

    /**
     * Pass-through method for Location.getLocationFromName(String)
     *
     * @param name the name of the location to find
     * @return the Location with the given name
     */
    public Location getLocationFromName(String name) {
        return locationManager.getLocationFromName(name);
    }

    /**
     * Pass-through method for Location.getLocationNames()
     *
     * @return a list of the names of all Locations
     */
    public List<String> getLocationNames() {
        return locationManager.getLocationNames();
    }
}

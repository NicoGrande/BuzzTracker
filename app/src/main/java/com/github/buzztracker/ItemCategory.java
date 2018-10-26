package com.github.buzztracker;

public enum ItemCategory {
    CLOTHING ("Clothing"),
    HAT ("Hat"),
    KITCHEN ("Kitchen"),
    ELECTRONICS ("Electronics"),
    HOUSEHOLD ("Household"),
    OTHER ("Other");

    private final String category;

    ItemCategory (String cat) {
        category = cat;
    }
}

package com.github.buzztracker.model;

/**
 * Category of Item
 */
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

    /**
     * Returns string version of category
     *
     * @return category string
     */
    public String getAsString() {
        return category;
    }
}

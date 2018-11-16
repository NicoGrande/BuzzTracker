package com.github.buzztracker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Inventory {

    private static final List<Item> inventory = new ArrayList<>();
    private static List<Item> filteredInventory = new ArrayList<>();

    static void addToInventory(Item item) {
        inventory.add(item);
    }

    static List<Item> getInventory() {
        return Collections.unmodifiableList(inventory);
    }

    static void setFilteredInventory(List<Item> list) {
        filteredInventory = new ArrayList<>(list);
    }

    static List<Item> getFilteredInventory() {
        return Collections.unmodifiableList(filteredInventory);
    }

    static void clear() {
        inventory.clear();
        filteredInventory.clear();
    }
}

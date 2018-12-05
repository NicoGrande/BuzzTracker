package com.github.buzztracker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class InventoryManager {

    public static InventoryManager getInstance() {
        return new InventoryManager();
    }

    private final List<Item> inventory = new ArrayList<>();
    private List<Item> filteredInventory = new ArrayList<>();

    void addToInventory(Item item) {
        inventory.add(item);
    }

     List<Item> getInventory() {
        return Collections.unmodifiableList(inventory);
    }

    void setFilteredInventory(List<Item> list) {
        filteredInventory = new ArrayList<>(list);
    }

    List<Item> getFilteredInventory() {
        return Collections.unmodifiableList(filteredInventory);
    }

    void clear() {
        inventory.clear();
        filteredInventory.clear();
    }
}

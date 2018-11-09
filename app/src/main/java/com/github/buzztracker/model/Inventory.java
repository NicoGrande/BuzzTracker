package com.github.buzztracker.model;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private static List<Item> inventory = new ArrayList<>();
    private static List<Item> filteredInventory = new ArrayList<>();

    static void addToInventory(Item item) {
        inventory.add(item);
    }

    static List<Item> getInventory() {
        return inventory;
    }

    static void setFilteredInventory(List<Item> list) {
        filteredInventory = list;
    }

    static List<Item> getFilteredInventory() {
        return filteredInventory;
    }

    static void clear() {
        inventory.clear();
        filteredInventory.clear();
    }
}

package com.github.buzztracker.model;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private static List<Item> inventory = new ArrayList<>();
    private static List<Item> filteredInventory = new ArrayList<>();

    public static void addToInventory(Item item) {
        inventory.add(item);
    }

    public static List<Item> getInventory() {
        return inventory;
    }

    public static void setFilteredInventory(List<Item> list) {
        filteredInventory = list;
    }

    public static List<Item> getFilteredInventory() {
        return filteredInventory;
    }

    public static void clear() {
        inventory.clear();
        filteredInventory.clear();
    }
}

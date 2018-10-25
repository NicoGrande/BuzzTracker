package com.github.buzztracker;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private static List<Item> inventory = new ArrayList<>();

    public static void addToInventory(Item item) {
        inventory.add(item);
    }

    public static List<Item> getInventory() {
        return inventory;
    }
}

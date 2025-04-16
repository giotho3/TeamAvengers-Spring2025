package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**Class: Model.ItemModel
 * Giovanni Thomas
 * Course: ITEC 3860 Spring 2025
 * Written: April 10, 2025.
 * The ItemModel Class is responsible for creating Item objects. These objects will consist of
 * Weapons, Armor and Consumables
 */

public abstract class Item {

    private final String type;
    private final String name;
    String desc;
    int feature;
    int location;

    public Item(String type, String name, String desc, int feature, int location) {
        this.type = type;
        this.name = name;
        this.desc = desc;
        this.feature = feature;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getFeature() {
        return feature;
    }

    abstract void applyFeature(Player player);

}
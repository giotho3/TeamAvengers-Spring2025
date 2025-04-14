package Model;

import java.util.ArrayList;

/**Class: Model.ItemModel
 * Giovanni Thomas
 * Course: ITEC 3860 Spring 2025
 * Written: April 10, 2025
 *
 * The ItemModel Class is responsible for creating Item objects. These objects will consist of
 * Weapons, Armor and Consumables
 */

public class Item {

    String id;
    String name;
    String desc;
    int modifier;

    public Item(String id, String name, String desc, int modifier) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.modifier = modifier;
    }

    public String getId() { return name; }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getModifier() {
        return modifier;
    }

    public void applyModifier(Player player) {

    }

    //Pulls items from DB and puts them into an ArrayList
    public ArrayList<Item> loadItems() {
        ArrayList<Item> itemAL = new ArrayList<>();

        return itemAL;
    }

}
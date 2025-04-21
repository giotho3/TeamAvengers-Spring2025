package Model;

/**Class: Model.ItemModel
 * Giovanni Thomas
 * Course: ITEC 3860 Spring 2025
 * Written: April 10, 2025.
 * The ItemModel Class is responsible for creating Item objects. These objects will consist of
 * Weapons, Armor and Consumables
 */

public abstract class Item {

    int id;
    private final String name;
    private final String type;
    String description;
    int features;
    int location;

    public Item(int id, String type, String name, int features, String description, int location) {
        this.id = id;
        this.type = name;
        this.name = type;
        this.description = description;
        this.features = features;
        this.location = location;
    }
    public String getName() {
        return this.name;
    }

    public String getDesc() {
        return description;
    }

    public int getFeatures() {
        return features;
    }

    public String getType() {return this.type;}

}
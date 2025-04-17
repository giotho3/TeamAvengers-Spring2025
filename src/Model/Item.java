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
    private final String type;
    private final String name;
    String description;
    String features;
    int location;

    public Item(int id, String type, String name, String features, String description, int location) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.features = features;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return description;
    }

    public String getFeatures() {
        return features;
    }

    abstract void applyFeature(Player player);

}
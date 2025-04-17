package Model;

public class ConcreteItem extends Item {
    public ConcreteItem(int id, String name, String type, String features, String description, int location) {
        super(id, name, type, features, description, location);
    }

    @Override
    void applyFeature(Player player) {
        // Implement the logic to apply the item's feature to the player
        System.out.println("Applying feature: " + getFeatures() + " to player.");
    }
}

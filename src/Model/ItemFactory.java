package Model;

public class ItemFactory {
    public static Item createItem(int id, String name, String type, String features, String description, int location) {
        //Instantiating the concrete subclass bc we want to keep Item abstract
        return new ConcreteItem(id, name, type, features, description, location);
    }
}
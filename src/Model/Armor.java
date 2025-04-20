package Model;

public class Armor extends Item {
    public Armor(int id, String type, String name, int modifier, String description, int location) {
        super(id, type, name, modifier, description, location);
    }
    @Override
    void applyFeature(Player player) {
        player.setArmor();
    }
}

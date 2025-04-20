package Model;

public class Weapon extends Item{

    public Weapon(int id, String type, String name, int modifier, String description, int location) {
        super(id, type, name, modifier, description, location);
    }

    @Override
    void applyFeature(Player player) {
        player.setDamage();
    }

}

package Model;

public class Weapon extends Item{

    public Weapon(String id, String name, String desc, int modifier, int location) {
        super(id, name, desc, modifier, location);
    }

    @Override
    void applyFeature(Player player) {
        player.setDamage();
    }


}

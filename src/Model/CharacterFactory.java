package Model;

public class CharacterFactory {
    public static Character createCharacter(int id, String name, int health, int attackPower) {
        return new ConcreteCharacter(id, name, health, attackPower);
        //keep abstraction, instantiate object
    }
}

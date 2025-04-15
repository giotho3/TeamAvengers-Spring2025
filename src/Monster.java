/* Aaron Matthews ITEC 3860- Dr. Rahaf Baraket

 */

public class Monster extends Character {
    private String description;

    public Monster(String name, int health, int attackPower, String description) {
        super(name, health, attackPower);
        this.description = description;
    }

    public void describe() {
        System.out.println(description);
    }
}

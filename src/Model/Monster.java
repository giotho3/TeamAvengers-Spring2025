package Model;/* Aaron Matthews ITEC 3860- Dr. Rahaf Baraket

 */

public class Monster extends Character
{
    private String description;

    public Monster(int id, String name, int health, int attackPower, String description) {
        super(id, name, health, attackPower);
        this.description = description;
    }

    public void describe() {
        System.out.println(description);
    }
}

package Model;/* Aaron Matthews ITEC 3860- Dr. Rahaf Baraket

 */

import java.util.Map;

public class Player extends Character
{
    private int currentRoom;

    public Player(String name, int health, int attackPower, int startRoom) {
        super(name, health, attackPower);
        this.currentRoom = startRoom;
    }

    public void move(String direction, Map<String, Room> rooms) {
        Room current = rooms.get(currentRoom);
        String nextRoom = current.getExits(direction);

        if (nextRoom != null) {
            currentRoom = nextRoom;
            System.out.println("You moved " + direction + " to " + nextRoom);
        } else {
            System.out.println("Movement not possible in that direction.");
        }
    }

    public String getCurrentRoom() {
        return currentRoom;
    }

    public void respawn() {
        System.out.println("Respawn method called");
    }

    public void setDamage(int modifier) {
        attackPower += modifier;
    }

}

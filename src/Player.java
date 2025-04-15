/* Aaron Matthews ITEC 3860- Dr. Rahaf Baraket

 */

import java.util.Map;

public class Player extends Character {
    private String currentRoom;

    public Player(String name, int health, int attackPower, String startRoom) {
        super(name, health, attackPower);
        this.currentRoom = startRoom;
    }

    public void move(String direction, Map<String, Room> rooms) {
        Room current = rooms.get(currentRoom);
        String nextRoom = current.getExit(direction);

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
}

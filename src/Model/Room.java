package Model;
import java.util.Map;

/**Class: Room.RoomModel
 * Peter Davis
 * Course: ITEC 3860 Spring 2025
 * Written: April 14, 2025
 * The RoomModel class is responsible for creating Room objects
 */
public class Room {
    int roomID;
    String roomName;
    String roomDesc;
    boolean hasCamp;
    Map<String, Integer> exits;
    boolean isVisited;

    public Room(int roomID, String roomName, String roomDesc, boolean hasCamp, Map<String, Integer> exits, boolean isVisited) {
        this.roomID = roomID;
        this.roomName = roomName;
        this.roomDesc = roomDesc;
        this.hasCamp = hasCamp;
        this.exits = exits;
        this.isVisited = isVisited;
    }

    public int getRoomID() {
        return roomID;
    }

    public int setRoomID(int roomID) {
        this.roomID = roomID;
        return roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomDesc() {
        return roomDesc;
    }

    public void setRoomDesc(String roomDesc) {
        this.roomDesc = roomDesc;
    }

    public Map<String, Integer> getExits() {
        return exits;
    }

    public void setExits(Map<String, Integer> exits) {
        this.exits = exits;
    }

    public boolean isHasCamp() {
        return hasCamp;
    }

    public void setHasCamp(boolean hasCamp) {
        this.hasCamp = hasCamp;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }
}

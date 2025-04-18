import Model.Room;
import java.util.Scanner;

public class Navigation {



    public static int navigate(String action, int roomNum) {
        Room currentRoom;

        try {
            //Scanner in = new Scanner(System.in);
                currentRoom = Fillers.getRoomById(roomNum + 1);
            assert currentRoom != null;
            System.out.println("Current room: " + currentRoom.getRoomID() +
                        ": " + currentRoom.getRoomName());
                if (currentRoom.isVisited()) {
                    System.out.println("You've been here before");
                }

                currentRoom.setVisited(true);
                if (action.equalsIgnoreCase("Look Around")) {
                    System.out.println(currentRoom.getRoomDesc());
                }
                if (currentRoom.getExits().containsKey(action.toUpperCase())) {
                    if (currentRoom.getExits().get(action) != 0) {
                        roomNum = currentRoom.getExits().get(action);
                    } else {
                        System.out.println("You can't go that way");
                    }
                    return roomNum;
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
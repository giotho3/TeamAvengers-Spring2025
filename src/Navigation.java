import Model.Room;

public class Navigation {



    public static int navigate(String action, int roomNum) {
        Room currentRoom;

        try {
                currentRoom = Fillers.getRoomById(roomNum + 1);
            assert currentRoom != null; //telling the compiler that it's impossible for currentRoom to be null
            System.out.println("Current room: " + currentRoom.getRoomID() +
                        ": " + currentRoom.getRoomName());
                if (currentRoom.isVisited()) {
                    System.out.println("You've been here before");
                }
                currentRoom.setVisited(true);
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
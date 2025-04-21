import Controller.Combat;
import Controller.PuzzleManager;
import Model.Monster;
import Model.Player;
import Model.Room;
import View.HelpMenu;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        boolean gameRunning = true;
        Utilities util = new Utilities();
        System.out.println(util.startMessage());

        Scanner input = new Scanner(System.in);
        String action;
        Room currentRoom = Fillers.getRoomById(1);
        assert currentRoom != null;
        int roomNum = currentRoom.setRoomID(1);
        Player player = new Player(50, 10, roomNum);

        while(gameRunning) {
            System.out.println("What would you like to do?");
            System.out.println("Type \"help\" for help");
            action = input.nextLine();

            if(action.equalsIgnoreCase("N") || action.equalsIgnoreCase("S") || action.equalsIgnoreCase("E") ||
                    action.equalsIgnoreCase("W") || action.equalsIgnoreCase("NE") || action.equalsIgnoreCase("NW") ||
                    action.equalsIgnoreCase("SW") || action.equalsIgnoreCase("SE")) {
                player.move(action);
                assert currentRoom != null;
                roomNum = Navigation.navigate(action, currentRoom.getRoomID());
                currentRoom = Fillers.getRoomById(roomNum);
            } else if (action.equals("look around")) {
                System.out.println(currentRoom.getRoomDesc());
            } else if (action.equals("save")) {
                player.saveGame();
            } else if (action.equals("fight")) {
                Combat combat = new Combat(player, Fillers.getMonsterFromID(roomNum));
            } else if (action.equals("grimoire")) {
                System.out.println(player.inventoryToString());
                //displays entire inventory, "-apparel", "-weapons", "-spells", "-potions"
            } else if (action.contains("wear") || action.contains("conjure")
                    || action.contains("drink") || action.contains("equip")) {
                String[] reqItem = action.split(" ");
                player.useItem(reqItem[1]);

            } else if (action.equals("interact")) {
                PuzzleManager pm =  new PuzzleManager();
                String userAnswer = input.next();
                pm.attemptPuzzle(currentRoom.getRoomID(), userAnswer);
                //hint goes in here after 3 attempts
            } else if (action.equals("load game")) {
                player.loadGame();
                //cancel or confirm for overwrite protection
            } else if (action.equals("help")) {
                    //open help menu. options include navigation, puzzle
                    //combat, items, story, save. exit will exit the menu
                System.out.println("Entering help menu");
                HelpMenu helpMenu = new HelpMenu();

                String userHelp = "";
                while (!userHelp.equalsIgnoreCase("exit")) {
                    helpMenu.displayHelp(userHelp);
                    userHelp = input.nextLine();
                }
                helpMenu.exitHelpMenu();
            } else if (action.equals("quit")){
                gameRunning = util.quit(input);
            } else {
                System.out.println("Invalid Command Entered. Type 'help' for help");
            }

        }


        System.out.println(util.endMessage());
    }
}

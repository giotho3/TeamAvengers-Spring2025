import Controller.PuzzleManager;
import Model.Player;
import Model.Room;
import View.HelpMenu;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        boolean gameRunning = true;
        Utilities util = new Utilities();
        System.out.println(util.startMessage());

        System.out.println(util.askForName());
        Scanner input = new Scanner(System.in);
        String action;
        Room currentRoom = Fillers.getRoomById(1);

        assert currentRoom != null;
        Player player = new Player(50, 10, currentRoom.getRoomID());

        while(gameRunning) {
            action = input.nextLine();

            if(action.equals("N") || action.equals("S") || action.equals("E") || action.equals("W")
            || action.equals("NE") || action.equals("NW") || action.equals("SW") || action.equals("SE")) {
                player.move(action);
                currentRoom = Navigation.navigate(action, currentRoom.getRoomID());
            } else if (action.equals("look around")) {
                System.out.println(currentRoom.getRoomDesc());
            } else if (action.equals("save")) {

            } else if (action.equals("fight")) {
                //attack and flee methods go in here
            } else if (action.equals("grimoire")) {
                //displays entire inventory, "-apparel", "-weapons", "-spells", "-potions"
            } else if (action.contains("wear") || action.contains("conjure")
                    || action.contains("drink") || action.contains("equip")) {
                String[] reqItem = action.split(" ");
                player.useItem(reqItem[1]); //wear cape

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
                String userHelp = input.nextLine();
                while (!userHelp.equalsIgnoreCase("exit")) {
                    helpMenu.displayHelp(userHelp);
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

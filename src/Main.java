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
        String action = input.next();
        int currentRoom = 1;

        Player player = new Player(action, 50, 10, currentRoom);

        while(gameRunning) {
            action = input.nextLine();

            if(action.equals("N") || action.equals("S") || action.equals("E") || action.equals("W")
            || action.equals("NE") || action.equals("NW") || action.equals("SW") || action.equals("SE")) {
                player.move(action);
                currentRoom = Navigation.navigate(action, currentRoom);
            } else if (action.equals("look around")) {

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
                //hint goes in here after 3 attempts
            } else if (action.equals("load game")) {
                //cancel or confirm for overwrite protection
            } else if (action.equals("help")) {
                    //open help menu. options include navigation, puzzle
                    //combat, items, story, save. exit will exit the menu
                System.out.println("Entering help menu");
                HelpMenu helpMenu = new HelpMenu();
                helpMenu.displayHelp(input.nextLine());
            } else if (action.equals("quit")){
                gameRunning = util.quit(input);
            } else {
                System.out.println("Invalid Command Entered. Type 'help' for help");
            }

        }


        System.out.println(util.endMessage());
    }
}

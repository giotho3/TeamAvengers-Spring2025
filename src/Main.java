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

        Player player = new Player(action, 50, 10, 1);

        while(gameRunning) {
            action = input.nextLine();

            if(action.equals("n") || action.equals("s") || action.equals("e") || action.equals("w")) {
                player.move(action);
            } else if (action.equals("drop")) {

            } else if (action.equals("look around")) {

            } else if (action.equals("save")) {

            } else if (action.equals("fight")) {
                //attack and flee methods go in here
            } else if (action.equals("grimoire")) {

            } else if (action.equals("wear " + KEYWORD)) {

            } else if (action.equals("equip " + KEYWORD)) {

            } else if (action.equals("conjure " + KEYWORD)) {

            } else if (action.equals("drink " + KEYWORD)) {

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

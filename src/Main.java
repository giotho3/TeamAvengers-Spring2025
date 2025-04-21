import Controller.Combat;
import Controller.PuzzleManager;
import Model.Monster;
import Model.Player;
import Model.Room;
import Model.Item;
import View.HelpMenu;

import java.util.Scanner;

/**
 * Class: Main
 * Giovanni Thomas
 * Course: ITEC 3860 Spring 2025
 * Written: April 20, 2025.
 * The Main class is responsible for calling the necessary methods for user input.
 */

public class Main {

    private static boolean activeCombat = false; // Prevent duplicate combat triggers

    public static void main(String[] args) {
        boolean gameRunning = true;
        Utilities util = new Utilities();
        System.out.println(util.startMessage());

        Scanner input = new Scanner(System.in);
        int startingRoom = 1;
        Room currentRoom = Fillers.getRoomById(startingRoom);
        assert currentRoom != null;
        Player player = new Player(500, 10, startingRoom);
        PuzzleManager puzzleManager = new PuzzleManager();

        // **Trigger automatic combat in Room 1 if a monster exists**
        Monster monster = Fillers.getMonsterFromID(startingRoom);
        if (monster != null) {
            System.out.println("\n⚔️ A wild " + monster.getName() + " appears!");
            activeCombat = true;
            new Combat(player, monster);
            activeCombat = false; // Reset combat flag after battle
        }

        while (gameRunning) {
            System.out.println("What would you like to do?");
            System.out.println("Type \"help\" for help");
            String action = input.nextLine();

            if (isDirection(action)) {
                currentRoom = Fillers.getRoomById(Navigation.navigate(action, currentRoom.getRoomID()));
                checkRoomContent(player, currentRoom, puzzleManager, input);

            } else if (action.equals("look around")) {
                System.out.println(currentRoom.getRoomDesc());

            } else if (action.equals("save")) {
                player.saveGame();

            } else if (action.equals("fight") && !activeCombat) {
                monster = Fillers.getMonsterFromID(currentRoom.getRoomID());
                if (monster != null) {
                    activeCombat = true; // Combat now active
                    new Combat(player, monster);
                    activeCombat = false; // Reset after battle
                } else {
                    System.out.println("There are no monsters here.");
                }

            } else if (action.equals("grimoire")) {
                System.out.println(player.inventoryToString());

            } else if (action.startsWith("pick up ")) {
                String itemName = action.substring(8).trim();
                Item item = Fillers.getItemFromName(itemName);
                if (item != null) {
                    player.pickUpItem(item);
                } else {
                    System.out.println("That item doesn't exist.");
                }

            } else if (action.contains("wear") || action.contains("conjure") || action.contains("drink") || action.contains("equip")) {
                String[] reqItem = action.split(" ");
                if (reqItem.length > 1) {
                    player.useItem(reqItem[1]);
                } else {
                    System.out.println("Specify an item to use.");
                }

            } else if (action.equals("interact")) {
                if (puzzleManager.hasPuzzle(currentRoom.getRoomID())) {
                    System.out.println("\n🤔 You found a puzzle! Here’s the challenge:");
                    System.out.println(puzzleManager.getPuzzleRiddle(currentRoom.getRoomID())); // Ensure riddle displays
                    String userAnswer = input.nextLine();
                    puzzleManager.attemptPuzzle(currentRoom.getRoomID(), userAnswer);
                } else {
                    System.out.println("There is no puzzle in this room.");
                }

            } else if (action.equals("load game")) {
                player.loadGame();

            } else if (action.equals("help")) {
                showHelpMenu(input);

            } else if (action.equals("quit")) {
                gameRunning = util.quit(input);

            } else {
                System.out.println("Invalid Command Entered. Type 'help' for help");
            }
        }

        System.out.println(util.endMessage());
        input.close(); // Ensuring Scanner closure to prevent resource leak
    }

    private static void checkRoomContent(Player player, Room room, PuzzleManager puzzleManager, Scanner input) {
        if (room == null) return;

        System.out.println("\n🔹 " + room.getRoomName());
        System.out.println(room.getRoomDesc());

        Item item = Fillers.getItemFromID(room.getRoomID());
        if (item != null) {
            System.out.println("\n🛠️ Items available:");
            System.out.println("- " + item.getName() + ": " + item.getDesc());
        } else {
            System.out.println("\n⚠️ There are no items in this room.");
        }

        if (puzzleManager.hasPuzzle(room.getRoomID())) {
            System.out.println("\n🤔 You found a puzzle! Here’s the challenge:");
            System.out.println(puzzleManager.getPuzzleRiddle(room.getRoomID())); // Ensure riddle displays
            String userAnswer = input.nextLine();
            puzzleManager.attemptPuzzle(room.getRoomID(), userAnswer);
        }

        Monster monster = Fillers.getMonsterFromID(room.getRoomID());
        if (monster != null && !activeCombat) {
            System.out.println("\n⚔️ A wild " + monster.getName() + " appears!");
            activeCombat = true;
            new Combat(player, monster);
            activeCombat = false; // Reset combat flag after battle
        }
    }

    private static boolean isDirection(String action) {
        return action.equalsIgnoreCase("N") || action.equalsIgnoreCase("S") || action.equalsIgnoreCase("E") ||
                action.equalsIgnoreCase("W") || action.equalsIgnoreCase("NE") || action.equalsIgnoreCase("NW") ||
                action.equalsIgnoreCase("SW") || action.equalsIgnoreCase("SE");
    }

    private static void showHelpMenu(Scanner input) {
        System.out.println("Entering help menu");
        HelpMenu helpMenu = new HelpMenu();
        String userHelp;
        while (true) {
            userHelp = input.nextLine();
            if (userHelp.equalsIgnoreCase("exit")) break;
            helpMenu.displayHelp(userHelp);
        }
        helpMenu.exitHelpMenu();
    }
}
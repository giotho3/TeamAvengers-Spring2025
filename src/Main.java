import Controller.Combat;
import Controller.PuzzleManager;
import Model.Monster;
import Model.Player;
import Model.Room;
import Model.Item;
import View.HelpMenu;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        boolean gameRunning = true;
        Utilities util = new Utilities();
        System.out.println(util.startMessage());

        Scanner input = new Scanner(System.in);
        String action;
        int startingRoom = 1; // Ensure Room 1 is the default starting room
        Room currentRoom = Fillers.getRoomById(startingRoom);
        assert currentRoom != null;
        Player player = new Player(500, 10, startingRoom); // Start player with 500 health
        PuzzleManager puzzleManager = new PuzzleManager(); // Use a single PuzzleManager instance

        // Display room details and items BEFORE any interactions
        displayRoomDetails(currentRoom);

        // NEW: Trigger combat immediately if Room 1 has a monster
        Monster monster = Fillers.getMonsterFromID(currentRoom.getRoomID());
        if (monster != null) {
            System.out.println("\n⚔️ A wild " + monster.getName() + " appears!");
            new Combat(player, monster);
        }

        while (gameRunning) {
            System.out.println("What would you like to do?");
            System.out.println("Type \"help\" for help");
            action = input.nextLine();

            if (isDirection(action)) {
                startingRoom = Navigation.navigate(action, currentRoom.getRoomID());
                currentRoom = Fillers.getRoomById(startingRoom);

                // NEW: Check for monsters, puzzles, and items when entering a room
                checkRoomContent(player, currentRoom, puzzleManager, input);

            } else if (action.equals("look around")) {
                assert currentRoom != null;
                System.out.println(currentRoom.getRoomDesc());
            } else if (action.equals("save")) {
                player.saveGame();
            } else if (action.equals("fight")) {
                Combat combat = new Combat(player, Fillers.getMonsterFromID(startingRoom));
            } else if (action.equals("grimoire")) {
                System.out.println(player.inventoryToString());
            } else if (action.startsWith("pick up ")) {
                String itemName = action.substring(8).trim(); // Extract item name
                Item item = Fillers.getItemFromName(itemName);
                if (item != null) {
                    player.pickUpItem(item);
                } else {
                    System.out.println("That item doesn't exist.");
                }

            } else if (action.contains("wear") || action.contains("conjure")
                    || action.contains("drink") || action.contains("equip")) {
                String[] reqItem = action.split(" ");
                player.useItem(reqItem[1]);

            } else if (action.equals("interact")) {
                assert currentRoom != null;
                System.out.println("Solve the puzzle:");
                String userAnswer = input.nextLine();
                puzzleManager.attemptPuzzle(currentRoom.getRoomID(), userAnswer);
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
    }

    /** Display room details and items upon entry */
    private static void displayRoomDetails(Room room) {
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
    }

    /** Check for monsters, puzzles, and items when entering a room */
    private static void checkRoomContent(Player player, Room room, PuzzleManager puzzleManager, Scanner input) {
        if (room == null) return;

        // Display room details and items BEFORE puzzles and combat
        displayRoomDetails(room);

        // Check for puzzles first
        if (puzzleManager.hasPuzzle(room.getRoomID())) {
            System.out.println("\n🤔 You found a puzzle! Try solving it:");
            String userAnswer = input.nextLine();
            puzzleManager.attemptPuzzle(room.getRoomID(), userAnswer);
        }

        // Check for monsters last
        Monster monster = Fillers.getMonsterFromID(room.getRoomID());
        if (monster != null) {
            System.out.println("\n⚔️ A wild " + monster.getName() + " appears!");
            new Combat(player, monster);
        }
    }

    /** Checks if the action is a valid movement direction */
    private static boolean isDirection(String action) {
        return action.equalsIgnoreCase("N") || action.equalsIgnoreCase("S") || action.equalsIgnoreCase("E") ||
                action.equalsIgnoreCase("W") || action.equalsIgnoreCase("NE") || action.equalsIgnoreCase("NW") ||
                action.equalsIgnoreCase("SW") || action.equalsIgnoreCase("SE");
    }

    /** Show the help menu */
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
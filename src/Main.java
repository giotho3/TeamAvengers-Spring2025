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
 * The Main class is responsible for handling user input and game logic.
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
            activeCombat = false;
        }

        player.setName("You"); // Assign player name

        while (gameRunning) {
            System.out.println("What would you like to do?");
            System.out.println("Type \"help\" for help");

            String action = input.nextLine().trim();

            if (isDirection(action)) {
                currentRoom = Fillers.getRoomById(Navigation.navigate(action, currentRoom.getRoomID()));
                checkRoomContent(player, currentRoom, puzzleManager, input);

            } else if (action.equals("look around")) {
                System.out.println(currentRoom.getRoomDesc());

            } else if (action.equals("save")) {
                player.saveGame();

            } else if (action.equals("load game")) { // ✅ Load game from database
                boolean success = player.loadGame();

                if (success) {
                    currentRoom = Fillers.getRoomById(player.getCurrentRoom()); // Reload saved room
                    System.out.println("✅ Game successfully loaded. You are in " + currentRoom.getRoomName());
                    checkRoomContent(player, currentRoom, puzzleManager, input);
                } else {
                    System.out.println("⚠️ Failed to load game. Check your save data.");
                }

            } else if (action.equals("fight") && !activeCombat) {
                monster = Fillers.getMonsterFromID(currentRoom.getRoomID());
                if (monster != null) {
                    activeCombat = true;
                    new Combat(player, monster);
                    activeCombat = false;
                } else {
                    System.out.println("There are no monsters here.");
                }

            } else if (action.equals("grimoire")) {
                System.out.println("📜 Inventory:\n" + player.inventoryToString());
                System.out.println("🛡️ Equipped Items: " + (player.getEquippedItems().isEmpty() ? "None" :
                        String.join(", ", player.getEquippedItems().stream().map(Item::getName).toList())));

            } else if (action.startsWith("pick up ")) {
                String itemName = action.substring(8).trim();
                Item item = Fillers.getItemFromName(itemName);

                if (item != null) {
                    player.pickUpItem(item); // ✅ Corrected method call
                    System.out.println("✅ You picked up " + item.getName() + "!");
                } else {
                    System.out.println("⚠️ That item doesn't exist.");
                }

            } else if (action.startsWith("wear ") || action.startsWith("equip ") || action.startsWith("use ") || action.startsWith("wield ")) {
                String itemName = action.substring(action.indexOf(" ") + 1).trim();
                Item item = Fillers.getItemFromName(itemName);

                if (item != null && player.getInventory().contains(item)) {
                    player.useItem(itemName);
                } else {
                    System.out.println("⚠️ You don’t have that item in your inventory.");
                }

            } else if (action.equals("interact")) {
                if (puzzleManager.hasPuzzle(currentRoom.getRoomID())) {
                    System.out.println("\n🤔 You found a puzzle! Here’s the challenge:");
                    System.out.println(puzzleManager.getPuzzleRiddle(currentRoom.getRoomID()));
                    String userAnswer = input.nextLine();
                    puzzleManager.attemptPuzzle(currentRoom.getRoomID(), userAnswer);
                } else {
                    System.out.println("There is no puzzle in this room.");
                }

            } else if (action.equals("help")) {
                showHelpMenu(input);

            } else if (action.equals("quit")) {
                gameRunning = util.quit(input);

            } else {
                System.out.println("Invalid Command Entered. Type 'help' for help");
            }
        }

        System.out.println(util.endMessage());
        input.close();
    }

    private static void checkRoomContent(Player player, Room room, PuzzleManager puzzleManager, Scanner input) {
        if (room == null) return;

        System.out.println("\n🔹 " + room.getRoomName());
        System.out.println(room.getRoomDesc());

        Item item = Fillers.getItemFromID(room.getRoomID());
        System.out.println("Debug: Checking for items in room " + room.getRoomID());
        if (item != null) {
            System.out.println("\n🛠️ Items available:");
            System.out.println("- " + item.getName() + ": " + item.getDesc());
        } else {
            System.out.println("\n⚠️ No items found in this room.");
        }

        if (puzzleManager.hasPuzzle(room.getRoomID())) {
            System.out.println("\n🤔 You found a puzzle! Here’s the challenge:");
            System.out.println(puzzleManager.getPuzzleRiddle(room.getRoomID()));
            String userAnswer = input.nextLine();
            puzzleManager.attemptPuzzle(room.getRoomID(), userAnswer);
        }

        Monster monster = Fillers.getMonsterFromID(room.getRoomID());
        if (monster != null && !activeCombat) {
            System.out.println("\n⚔️ A wild " + monster.getName() + " appears!");
            activeCombat = true;
            new Combat(player, monster);
            activeCombat = false;
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
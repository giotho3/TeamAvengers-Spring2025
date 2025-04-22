package Controller;

import Model.Monster;
import Model.Player;
import java.util.Scanner;

public class Combat {
    private Player player;
    private Monster monster;
    private boolean isPlayerTurn;
    private boolean isBlocking;
    private boolean battleActive;
    private Scanner scanner = new Scanner(System.in);

    public Combat(Player player, Monster monster) {
        this.player = player;
        this.monster = monster;
        this.isPlayerTurn = true;
        this.isBlocking = false;
        this.battleActive = true;
        startBattle();
    }

    /** Main combat loop **/
    private void startBattle() {
        System.out.println("A battle begins between you and " + monster.getName() + "!");

        while (battleActive && player.getHealth() > 0 && monster.getHealth() > 0) {
            if (isPlayerTurn) {
                playerTurn();
            } else {
                monsterTurn();
            }
            isPlayerTurn = !isPlayerTurn;
        }

        if (battleActive) { // Prevent premature battle conclusion print if fleeing
            endBattle();
        }
    }

    /** Player's turn with input validation **/
    private void playerTurn() {
        int choice = -1;
        while (true) {
            System.out.println("\nChoose action: [1] Attack  [2] Block  [3] Flee");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Clear newline character
                break;
            } else {
                System.out.println("Invalid input. Please enter a number: [1] Attack  [2] Block  [3] Flee");
                scanner.nextLine(); // Clear invalid input
            }
        }

        switch (choice) {
            case 1 -> {
                System.out.println("You attack!");
                player.attack(monster);
                isBlocking = false;
            }
            case 2 -> {
                System.out.println(player.getName() + " braces for impact!");
                isBlocking = true;
            }
            case 3 -> {
                System.out.println("You flee the battle!");
                battleActive = false; // **Breaks out of combat loop**
            }
            default -> System.out.println("Invalid choice.");
        }
    }

    /** Monster's turn with AI behavior **/
    private void monsterTurn() {
        if (!battleActive) return; // Ensures monster doesn't act after fleeing

        int action = (int) (Math.random() * 3);
        switch (action) {
            case 0 -> {
                int baseDamage = monster.getAttackPower();
                int finalDamage = isBlocking ? baseDamage / 2 : baseDamage;
                System.out.println(monster.getName() + " attacks!");
                player.takeDamage(finalDamage);
            }
            case 1 -> System.out.println(monster.getName() + " growls menacingly, preparing a stronger attack!");
            case 2 -> System.out.println(monster.getName() + " hesitates, watching its opponent closely.");
        }
        isBlocking = false;
    }

    private void endBattle() {
        if (player.getHealth() <= 0) {
            System.out.println(player.getName() + " has been defeated!");
            player.loadGame();
        } else if (monster.getHealth() <= 0) {
            System.out.println(monster.getName() + " was vanquished!");
            monster.die();
            player.saveGame();
        }
    }
}
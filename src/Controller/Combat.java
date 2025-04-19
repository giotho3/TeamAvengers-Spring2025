package Controller;

import Model.Monster;
import Model.Player;
import java.util.Scanner;

public class Combat {
    private Player player;
    private Monster monster;
    private boolean isPlayerTurn;
    private Scanner scanner = new Scanner(System.in);

    public Combat(Player player, Monster monster) {
        this.player = player;
        this.monster = monster;
        this.isPlayerTurn = true; // Player starts first
        startBattle();
    }

    /** Main turn-based combat loop **/
    private void startBattle() {
        System.out.println("A battle begins between " + player.getName() + " and " + monster.getName() + "!");

        while (player.getHealth() > 0 && monster.getHealth() > 0) {
            if (isPlayerTurn) {
                playerTurn();
            } else {
                monsterTurn();
            }
            isPlayerTurn = !isPlayerTurn; // Toggle turn
        }

        endBattle();
    }

    /** Handles player's turn **/
    private void playerTurn() {
        System.out.println("\nChoose action: [1] Attack  [2] Block  [3] Flee");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> {
                System.out.println(player.getName() + " attacks!");
                player.attack(monster);
            }
            case 2 -> System.out.println(player.getName() + " blocks!");
            case 3 -> {
                System.out.println(player.getName() + " flees the battle!");
                return;
            }
            default -> System.out.println("Invalid choice.");
        }
    }


    /** Handles monster's turn **/
    private void monsterTurn() {
        int damage = monster.getDifficulty().equalsIgnoreCase("Hard") ? 80 : 30; // Hard monsters hit harder
        System.out.println(monster.getName() + " attacks!");
        player.takeDamage(damage);
    }

    /** Process battle outcome **/
    private void endBattle() {
        if (player.getHealth() <= 0) {
            System.out.println(player.getName() + " has been defeated!");
            player.respawn(); // Respawn player
        } else if (monster.getHealth() <= 0) {
            System.out.println(monster.getName() + " was vanquished!");
            monster.die(); // Remove monster from database
            player.savePlayerState(); // Persist battle progress
        }
    }
}
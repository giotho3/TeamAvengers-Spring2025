package Controller;/* Aaron Matthews ITEC 3860- Dr. Rahaf Baraket

 */

import Model.Monster;
import Model.Player;

import java.util.Scanner;

public class Combat {
    private final Player player;
    private Monster monster;
    private boolean battleActive;
    private Scanner scanner;

    public Combat(Player player, Monster monster) {
        this.player = player;
        this.monster = monster;
        this.battleActive = true;
        this.scanner = new Scanner(System.in);
        startBattle();
    }

    private void startBattle() {
        System.out.println("A battle begins between " + player.getName() + " and " + monster.getName() + "!");

        while (battleActive) {
            playerTurn();
            if (battleActive) {
                monsterTurn();
            }
        }
    }

    private void playerTurn() {
        System.out.println("\nYour Turn! Choose an action: [Attack] [Flee]");
        String action = scanner.nextLine().toLowerCase();

        switch (action) {
            case "attack":
                player.attack(monster);
                checkBattleStatus();
                break;
            case "flee":
                if (attemptFlee()) {
                    System.out.println("You successfully fled the battle!");
                    battleActive = false;
                } else {
                    System.out.println("Escape failed! The battle continues.");
                }
                break;
            default:
                System.out.println("Invalid action! Choose [Attack] or [Flee].");
                playerTurn(); // Ask again for valid input
        }
    }

    private void monsterTurn() {
        if (battleActive) {
            System.out.println("\n" + monster.getName() + "'s Turn!");
            monster.attack(player);
            checkBattleStatus();
        }
    }

    private boolean attemptFlee() {
        int fleeChance = (int) (Math.random() * 100); // 50% chance to escape
        return fleeChance > 50;
    }

    private void checkBattleStatus() {
        if (player.getHealth() <= 0) {
            System.out.println("You have been defeated!");
            battleActive = false;
            triggerRespawn();
        } else if (monster.getHealth() <= 0) {
            System.out.println(monster.getName() + " has been defeated!");
            battleActive = false;
        }
    }

    private void triggerRespawn() {
        System.out.println("Respawning player at last save point...");
        player.respawn(); // Ensure Model.Player class has a respawn method
    }
}
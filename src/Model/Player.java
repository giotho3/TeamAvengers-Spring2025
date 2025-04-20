package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Player extends Character {
    private int currentRoom;
    int damage;
    int armor;
    private List<Item> inventory;
    private static final String DB_URL = "jdbc:sqlite:identifier.sqlite";
    private static final Logger LOGGER = Logger.getLogger(Player.class.getName());


    /** Getters for external use **/
    public int getCurrentRoom() { return currentRoom; }
    public void setCurrentRoom(int currentRoom) { this.currentRoom = currentRoom; }

    public List<Item> getInventory() { return inventory; }
    public void setInventory(List<Item> inventory) { this.inventory = inventory; }

    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = Math.max(health, 0); } // Prevents negative health

    public int getAttackPower() { return attackPower; }
    public void setAttackPower(int attackPower) { this.attackPower = Math.max(attackPower, 0); } // Prevents negative attack power

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Player(int id, String name, int health, int attackPower, int startRoom) {
        super(id, name, health, attackPower);
        this.currentRoom = startRoom;
        this.inventory = new ArrayList<>();
    }

    /** Convert inventory to a storable format (comma-separated values) **/
    private String inventoryToString() {
        return inventory.isEmpty() ? "" : String.join(",", inventory.stream().map(Item::getName).toList());
    }

    /** Parse stored inventory string **/
    private List<Item> parseInventory(String inventoryData) {
        List<Item> parsedInventory = new ArrayList<>();

        if (inventoryData == null || inventoryData.isBlank()) return parsedInventory;

        String[] itemNames = inventoryData.split(",");
        for (String itemName : itemNames) {
            parsedInventory.add(new ConcreteItem(itemName.trim())); // Assuming `Item` has a constructor taking a name
        }
        return parsedInventory;
    }

    /** Save player state **/
    public void saveGame() {
        String query = """
                INSERT INTO PlayerState (player_id, current_room, inventory) 
                VALUES (?, ?, ?, ?) 
                ON CONFLICT(player_id) DO UPDATE 
                SET current_room=?, inventory=?""";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, this.id);
                stmt.setInt(2, this.currentRoom);
                stmt.setString(3, inventoryToString());
                stmt.setInt(4, this.health);
                stmt.setInt(5, this.currentRoom);
                stmt.setString(6, inventoryToString());
                stmt.setInt(7, this.health);

                stmt.executeUpdate();
                conn.commit();
                System.out.println("Game saved successfully.");

            } catch (SQLException e) {
                conn.rollback();
                LOGGER.severe("Error saving player state: " + e.getMessage());
            }
        } catch (SQLException e) {
            LOGGER.severe("Database connection error: " + e.getMessage());
        }
    }

    /** Load player state **/
    public boolean loadGame() {
        String query = "SELECT * FROM PlayerState WHERE player_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, this.id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                this.currentRoom = rs.getInt("current_room");
                this.inventory = parseInventory(rs.getString("inventory"));
                this.health = rs.getInt("health");
                System.out.println("Game loaded at room: " + this.currentRoom);
                return true;
            } else {
                System.out.println("No saved data found.");
                return false;
            }
        } catch (SQLException e) {
            LOGGER.severe("Error loading player state: " + e.getMessage());
        }
        return false;
    }

    /** Movement system **/
    public void move(String direction) {
        String query = "SELECT " + direction + "_exit FROM Rooms WHERE room_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, currentRoom);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int nextRoomID = rs.getInt(1);
                if (nextRoomID != 0) {
                    currentRoom = nextRoomID;
                    saveGame();
                    System.out.println("Moved to room: " + currentRoom);
                } else {
                    System.out.println("Cannot move " + direction + ". The exit is locked.");
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error moving player: " + e.getMessage());
        }
    }

    /** Respawn player **/
    public void respawn() {
        currentRoom = 0;
        health = 100;
        System.out.println("Respawning...");
        saveGame();
    }

    /** Use an item **/
    public void useItem(String reqItem) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(reqItem)) {
                switch (item.getType()) {
                    case "Armor" -> {
                        health += item.getFeatures();
                        System.out.println("Equipped " + item.getName() + ".");
                    }
                    case "Weapons" -> {
                        attackPower += item.getFeatures();
                        System.out.println("Wielded " + item.getName() + ".");
                    }
                    case "Potions" -> {
                        health += item.getFeatures();
                        inventory.remove(item); // Potions are consumables
                    }
                    default -> System.out.println("Used " + item.getName() + ".");
                }
            }
        }
        saveGame();
    }

    /** Pick up an item **/
    public void pickUpItem(Item item) {
        if (item == null) {
            System.out.println("No valid item selected.");
            return;
        }

        inventory.add(item);
        System.out.println("Picked up " + item.getName() + ".");
        saveGame();
    }



    public void setDamage() {
        this.damage = damage;
    }
    public void setArmor() {
        this.armor = armor;
    }
}
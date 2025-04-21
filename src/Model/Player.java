package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Player extends Character {
    private int currentRoom;
    private int damage;
    private int armor;
    private List<Item> inventory;
    private List<Item> equippedItems;  // New list for equipped items
    public final String DB_URL = "jdbc:sqlite:identifier.sqlite";
    private static final Logger LOGGER = Logger.getLogger(Player.class.getName());
    private String player;

    public Player(int health, int attackPower, int startRoom) {
        super(health, attackPower);
        this.currentRoom = startRoom;
        this.inventory = new ArrayList<>();
        this.equippedItems = new ArrayList<>();
        this.name = "You"; // Ensure default name is assigned
    }

    /** Getters for external use **/
    /** Set the player's name **/
    public void setName(String name) {
        this.name = name != null && !name.isBlank() ? name.trim() : "Unknown"; // Prevent empty names
    }
    public List<Item> getInventory() { return inventory; }
    public List<Item> getEquippedItems() { return equippedItems; }  // New getter
    public int getCurrentRoom() { return currentRoom; }
    public void setCurrentRoom(int currentRoom) { this.currentRoom = currentRoom; }
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = Math.max(health, 0); }
    public int getAttackPower() { return attackPower; }
    public void setAttackPower(int attackPower) { this.attackPower = Math.max(attackPower, 0); }

    /** Inventory Display Method **/
    public String inventoryToString() {
        String inv = inventory.isEmpty() ? "Your inventory is empty." : String.join(", ", inventory.stream().map(Item::getName).toList());
        String equipped = equippedItems.isEmpty() ? "No equipped items." : String.join(", ", equippedItems.stream().map(Item::getName).toList());
        return "Inventory: " + inv + "\nEquipped Items: " + equipped;
    }
    public void pickUpItem(Item item) {
        if (item != null) {
            inventory.add(item); // ✅ Adds item to player's inventory
            System.out.println("✅ You picked up " + item.getName() + "!");
        } else {
            System.out.println("⚠️ That item doesn't exist.");
        }
    }

    /** Parse stored inventory string **/
    private List<Item> parseInventory(String inventoryData) {
        List<Item> parsedInventory = new ArrayList<>();

        if (inventoryData == null || inventoryData.isBlank()) return parsedInventory;

        String[] itemNames = inventoryData.split(",");
        for (String name : itemNames) {
            Item item = ItemFactory.createItem(name.trim()); // Use factory method for item creation
            if (item != null) {
                parsedInventory.add(item);
            }
        }
        return parsedInventory;
    }

    /** Equip an Item **/
    public void useItem(String reqItem) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(reqItem)) {
                switch (item.getType()) {
                    case "Armor" -> {
                        health += item.getFeatures();
                        equippedItems.add(item);  // Move to equipped list
                        inventory.remove(item);   // Remove from regular inventory
                        System.out.println("Equipped " + item.getName() + ".");
                    }
                    case "Weapons" -> {
                        attackPower += item.getFeatures();
                        equippedItems.add(item);
                        inventory.remove(item);
                        System.out.println("Wielded " + item.getName() + ".");
                    }
                    default -> System.out.println("Used " + item.getName() + ".");
                }
                break; // Stop after equipping one item
            }
        }
        saveGame();
    }

    /** Save Game **/
    public void saveGame() {
        String query = """
        INSERT INTO PlayerState (player_id, current_room, inventory, last_save, player_damage, player_health) 
        VALUES (?, ?, ?, ?, ?, ?) 
        ON CONFLICT(player_id) DO UPDATE 
        SET current_room=?, inventory=?, last_save=?, player_damage=?, player_health=?""";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, this.id);
                stmt.setInt(2, this.currentRoom);
                stmt.setString(3, inventoryToString());
                stmt.setString(4, String.join(", ", equippedItems.stream().map(Item::getName).toList()));  // Store equipped items
                stmt.setInt(5, this.currentRoom);
                stmt.setString(6, inventoryToString());
                stmt.setString(7, String.join(", ", equippedItems.stream().map(Item::getName).toList()));

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
                this.equippedItems = parseInventory(rs.getString("equipped_items")); // Load equipped items correctly // Load equipped items separately
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
}
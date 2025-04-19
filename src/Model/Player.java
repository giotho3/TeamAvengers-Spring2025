package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Player extends Character {
    private int currentRoom;
    private List<Item> inventory;
    private Timestamp lastSave;

    private static final String DB_URL = "jdbc:sqlite:identifier.sqlite";

    public Player(String name, int health, int attackPower, int startRoom) {
        super(id, name, health, attackPower);
        this.currentRoom = startRoom;
        this.inventory = new ArrayList<>();
        this.lastSave = null;
    }

    /** SAVE player state to database **/
    public void savePlayerState() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO PlayerState (player_id, current_room, inventory, last_save) VALUES (?, ?, ?, ?) "
                             + "ON CONFLICT(player_id) DO UPDATE SET current_room = ?, inventory = ?, last_save = ?")) {

            Timestamp saveTime = new Timestamp(System.currentTimeMillis());

            stmt.setInt(1, this.id);
            stmt.setInt(2, this.currentRoom);
            stmt.setString(3, inventoryToString());
            stmt.setTimestamp(4, saveTime);
            stmt.setInt(5, this.currentRoom);
            stmt.setString(6, inventoryToString());
            stmt.setTimestamp(7, saveTime);

            stmt.executeUpdate();
            this.lastSave = saveTime;
            System.out.println("Game saved successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** LOAD player state **/
    public boolean loadPlayerState() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM PlayerState WHERE player_id = ?")) {

            stmt.setInt(1, this.id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                this.currentRoom = rs.getInt("current_room");
                this.inventory = parseInventory(rs.getString("inventory"));
                this.lastSave = rs.getTimestamp("last_save");
                System.out.println("Game loaded at room: " + this.currentRoom);
                return true;
            } else {
                System.out.println("No saved data found.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Convert inventory list to a storable format **/
    private String inventoryToString() {
        return String.join(",", inventory.stream().map(Item::getName).toList());
    }

    /** Parse stored inventory string **/
    private List<Item> parseInventory(String inventoryData) {
        List<Item> parsedInventory = new ArrayList<>();

        if (inventoryData != null && !inventoryData.isBlank()) { // Handles empty or malformed input safely
            String[] itemDataArray = inventoryData.split(",");

            for (String itemData : itemDataArray) {
                itemData = itemData.trim(); // Remove any accidental whitespace

                if (!itemData.isEmpty()) {
                    // Extract item attributes (assuming comma-separated values include type info)
                    String[] attributes = itemData.split(":"); // Example format: "id:name:type:features:description:location"
                    if (attributes.length >= 6) { // Ensure sufficient attributes exist
                        int id = Integer.parseInt(attributes[0]);
                        String name = attributes[1];
                        String type = attributes[2];
                        String features = attributes[3];
                        String description = attributes[4];
                        int location = Integer.parseInt(attributes[5]);

                        Item newItem = ItemFactory.createItem(id, name, type, features, description, location);
                        parsedInventory.add(newItem);
                    } else {
                        System.out.println("Invalid inventory data format: " + itemData);
                    }
                }
            }
        }

        return parsedInventory;
    }

    /** Movement system **/
    public void move(String direction) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement("SELECT " + direction + "_exit FROM Rooms WHERE room_id = ?")) {

            stmt.setInt(1, currentRoom);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int nextRoomID = rs.getInt(1);
                if (nextRoomID != 0) {
                    currentRoom = nextRoomID;
                    savePlayerState();
                    System.out.println("Moved to room: " + currentRoom);
                } else {
                    System.out.println("Cannot move " + direction + ". The exit is locked.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Respawn player **/
    public void respawn() {
        currentRoom = 0;
        health = 100;
        System.out.println("Respawning at last save point...");
        savePlayerState();
    }

    public void useItem(Item item) {
        switch (item.getType()) {
            case "Apparel":
                health += item.getEffectValue(); // Boost defense
                System.out.println("Equipped " + item.getName() + ".");
                break;
            case "Weapons":
                attackPower += item.getEffectValue(); // Boost attack strength
                System.out.println("Wielded " + item.getName() + ".");
                break;
            case "Spells":
                castSpell(item); // Trigger magic effects
                break;
            case "Potions":
                health += item.getEffectValue();
                inventory.remove(item); // Potions are consumables
                break;
            default:
                System.out.println("Used " + item.getName() + ".");
        }
    }

    public void pickUpItem(Item item) {
        if (item == null) {
            System.out.println("No valid item selected.");
            return;
        }

        inventory.add(item);
        System.out.println("Picked up " + item.getName() + ".");

        // Remove item from room or mark it as picked up
        removeItemFromRoom(item.getId());

        savePlayerState(); // Ensure inventory is saved
    }
}
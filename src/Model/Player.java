package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Player extends Character {
    private int currentRoom;
    private List<Item> inventory;
    private Timestamp lastSave;
    private static final String DB_URL = "jdbc:sqlite:identifier.sqlite";

    public Player(int id, String name, int health, int attackPower, int startRoom) {
        super(id, name, health, attackPower);
        this.currentRoom = startRoom;
        this.inventory = new ArrayList<>();
        this.lastSave = null;
    }

    /** JSON Inventory Serialization **/
    private String inventoryToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Item item : inventory) {
            JSONObject jsonItem = new JSONObject();
            jsonItem.put("id", item.getId());
            jsonItem.put("name", item.getName());
            jsonItem.put("type", item.getType());
            jsonArray.put(jsonItem);
        }
        return jsonArray.toString();
    }

    /** Save Player State **/
    public void savePlayerState() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO PlayerState (player_id, current_room, inventory, last_save) VALUES (?, ?, ?, ?) " +
                            "ON CONFLICT(player_id) DO UPDATE SET current_room=?, inventory=?, last_save=?")) {

                Timestamp saveTime = new Timestamp(System.currentTimeMillis());
                stmt.setInt(1, this.id);
                stmt.setInt(2, this.currentRoom);
                stmt.setString(3, inventoryToJson());
                stmt.setTimestamp(4, saveTime);
                stmt.setInt(5, this.currentRoom);
                stmt.setString(6, inventoryToJson());
                stmt.setTimestamp(7, saveTime);

                stmt.executeUpdate();
                conn.commit();
                this.lastSave = saveTime;
                System.out.println("Game saved successfully.");

            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Error saving player state: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
    }
}
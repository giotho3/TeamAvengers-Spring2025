import Model.Monster;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Fillers {
    private static final String DB_URL = "jdbc:sqlite:identifier.sqlite";

    public static Model.Room getRoomById(int roomId) {
        String query = "SELECT * FROM ROOMS WHERE room_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("room_id");
                String name = rs.getString("room_name");
                String description = rs.getString("room_desc");
                boolean camp = rs.getInt("camp") == 1;
                boolean visited = rs.getInt("visited") == 1;

                // Load exits into a map
                Map<String, Integer> exits = new HashMap<>();
                exits.put("N", rs.getInt("north_exit"));
                exits.put("S", rs.getInt("south_exit"));
                exits.put("E", rs.getInt("east_exit"));
                exits.put("W", rs.getInt("west_exit"));
                exits.put("NE", rs.getInt("northeast_exit"));
                exits.put("NW", rs.getInt("northwest_exit"));
                exits.put("SE", rs.getInt("southeast_exit"));
                exits.put("SW", rs.getInt("southwest_exit"));

                return new Model.Room(id, name, description, camp, exits, visited);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Model.Item getItemFromID(int itemId) {
        String query = "SELECT * FROM ITEMS WHERE item_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, itemId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("item_id");
                String name = rs.getString("item_name");
                String type = rs.getString("item_type");
                String features = rs.getString("item_features");
                String description = rs.getString("item_desc");
                int location = rs.getInt("item_location");

                return Model.ItemFactory.createItem(id, name, type, features, description, location);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Model.Monster getMonsterFromID(int monsterID) {
        String query = "SELECT * FROM MONSTERS WHERE monster_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, monsterID);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                int id = rs.getInt("monster_id");
                String difficulty = rs.getString("monster_difficulty");
                String name = rs.getString("monster_name");
                String desc = rs.getString("monster_description");
                String health = "";
                String damage = "";

                if (difficulty.equalsIgnoreCase("easy")) {
                    //what we want hp and damage for an easy mob to be
                } else if (difficulty.equalsIgnoreCase("hard")) {
                    //what we want hp and damage for a hard mob to be
                }
                int location = rs.getInt("room_number");
                return new Monster(id, name, health, damage, desc, location);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

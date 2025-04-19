package Model;

import java.sql.*;

public class Monster extends Character {
    private String type;
    private String description;
    private String difficulty; // Now stored as "Easy" or "Hard"
    private int roomNumber;

    private static final String DB_URL = "jdbc:sqlite:identifier.sqlite";

    public Monster(int id, String name, int health, int attackPower, String type, String difficulty, String description, int roomNumber) {
        super(id, name, health, attackPower);
        this.type = type;
        this.difficulty = difficulty;
        this.description = description;
        this.roomNumber = roomNumber;
    }

    public String getType() { return type; }
    public String getDescription() { return description; }
    public String getDifficulty() { return difficulty; }

    /** Load a monster dynamically when entering a room **/
    public static Monster loadMonster(int roomNumber) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Monsters WHERE room_number = ?")) {

            stmt.setInt(1, roomNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String difficulty = rs.getString("monster_difficulty");

                return new Monster(
                        rs.getInt("monster_id"),
                        rs.getString("monster_name"),
                        adjustHealthBasedOnDifficulty(difficulty),
                        adjustAttackBasedOnDifficulty(difficulty),
                        rs.getString("monster_type"),
                        difficulty,
                        rs.getString("monster_description"),
                        rs.getInt("room_number")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Adjust health based on difficulty **/
    private static int adjustHealthBasedOnDifficulty(String difficulty) {
        return difficulty.equalsIgnoreCase("Hard") ? 200 : 150;
    }

    /** Adjust attack power based on difficulty **/
    private static int adjustAttackBasedOnDifficulty(String difficulty) {
        return difficulty.equalsIgnoreCase("Hard") ? 80 : 30;
    }

    /** Override defeat logic to remove monster from room **/
    @Override
    public void die() {
        System.out.println(name + " has been slain!");
        removeMonsterFromDatabase();
    }

    /** Removes monster from the database after defeat **/
    private void removeMonsterFromDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM Monsters WHERE room_number = ?")) {

            stmt.setInt(1, roomNumber);
            stmt.executeUpdate();
            System.out.println(name + " removed from room " + roomNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Monster extends Character {
    private String type;
    private String description;
    private String difficulty;
    private int roomNumber;
    private static final Logger LOGGER = Logger.getLogger(Monster.class.getName());
    private static final String DB_URL = "jdbc:sqlite:identifier.sqlite";

    public Monster(int id, String name, String type, String difficulty, String description, int roomNumber) {
        super(id, name, adjustHealthBasedOnDifficulty(difficulty), adjustAttackBasedOnDifficulty(difficulty));
        this.type = type;
        this.difficulty = difficulty;
        this.description = description;
        this.roomNumber = roomNumber;
    }

    public String getType() { return type; }
    public String getDescription() { return description; }
    public String getDifficulty() { return difficulty; }

    /** Adjust health based on difficulty **/
    private static int adjustHealthBasedOnDifficulty(String difficulty) {
        return "Hard".equalsIgnoreCase(difficulty) ? 200 : 150;
    }

    /** Adjust attack power based on difficulty **/
    private static int adjustAttackBasedOnDifficulty(String difficulty) {
        return "Hard".equalsIgnoreCase(difficulty) ? 80 : 30;
    }

    /** Handle defeat logic **/
    @Override
    public void die() {
        System.out.println(getName() + " has been slain!");
        removeMonsterFromDatabase();
    }

    /** Removes monster from the database after defeat **/
    /** Marks monster as removed by setting its room_number to 0 **/
    private void removeMonsterFromDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement("UPDATE Monsters SET room_number = 0 WHERE room_number = ?")) {
                stmt.setInt(1, roomNumber);
                int affectedRows = stmt.executeUpdate();
                conn.commit();

                if (affectedRows > 0) {
                    System.out.println(getName() + " has been removed from room " + roomNumber);
                } else {
                    System.out.println("No monster found in room " + roomNumber);
                }


            } catch (SQLException e) {
                conn.rollback();
                LOGGER.severe("Failed to update monster: " + e.getMessage());
            }
        } catch (SQLException e) {
            LOGGER.severe("Database connection error: " + e.getMessage());
        }
    }
}
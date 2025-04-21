package Controller;

import Model.Puzzle;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

public class PuzzleManager {
    private static final String DB_URL = "jdbc:sqlite:identifier.sqlite";
    private static final Logger LOGGER = Logger.getLogger(PuzzleManager.class.getName());
    private Map<Integer, Puzzle> puzzles;

    public PuzzleManager() {
        puzzles = new HashMap<>();
    }

    /** Retrieve a puzzle for a specific room (Lazy Loading) **/
    public Puzzle getPuzzleFromRoom(int roomNumber) {
        if (puzzles.containsKey(roomNumber)) {
            return puzzles.get(roomNumber);
        }

        String query = "SELECT * FROM Puzzle WHERE room_number = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, roomNumber); // Set the room number before executing the query

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Puzzle puzzle = new Puzzle(
                            rs.getString("puzzle_name"),
                            rs.getString("puzzle_answer"),
                            rs.getString("puzzle_riddle"),
                            rs.getString("puzzle_reward"),
                            rs.getString("puzzle_desc")
                    );
                    puzzles.put(roomNumber, puzzle);
                    return puzzle;
                } else {
                    // **Only warn if room was expected to contain a puzzle**
                    if (expectedPuzzle(roomNumber)) {
                        LOGGER.warning("Puzzle retrieval failed for room " + roomNumber + ": No puzzle found.");
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching puzzle for room {0}: {1}", new Object[]{roomNumber, e.getMessage()});
        }
        return null;
    }

    /** Retrieve the puzzle riddle **/
    public String getPuzzleRiddle(int roomNumber) {
        Puzzle puzzle = getPuzzleFromRoom(roomNumber);
        return (puzzle != null) ? puzzle.getRiddle() : "No puzzle found in this room.";
    }

    /** Check if the room has a puzzle **/
    public boolean hasPuzzle(int roomID) {
        return puzzles.containsKey(roomID) || getPuzzleFromRoom(roomID) != null;
    }

    /** Determine if a puzzle was expected in this room **/
    private boolean expectedPuzzle(int roomNumber) {
        // Logic to check if this room *should* contain a puzzle (could reference a predefined list)
        return roomNumber == 5 || roomNumber == 8 || roomNumber == 12; // Example expected puzzle rooms
    }

    /** Attempt to solve a puzzle **/
    public boolean attemptPuzzle(int roomNumber, String attempt) {
        Puzzle puzzle = getPuzzleFromRoom(roomNumber);
        if (puzzle == null) {
            System.out.println("No puzzle exists in this room.");
            return false;
        }

        boolean success = puzzle.attemptSolution(attempt);
        if (success) {
            markPuzzleAsSolved(roomNumber);
            unlockRoomAfterPuzzle(roomNumber);
        }
        return success;
    }

    /** Mark a puzzle as solved in SQLite **/
    private void markPuzzleAsSolved(int roomNumber) {
        String query = "UPDATE Puzzle SET post_interaction = 'solved' WHERE room_number = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, roomNumber);
            stmt.executeUpdate();
            System.out.println("Puzzle in room " + roomNumber + " marked as solved.");
        } catch (SQLException e) {
            LOGGER.severe("Error marking puzzle as solved: " + e.getMessage());
        }
    }

    /** Unlock room exits after puzzle completion **/
    private void unlockRoomAfterPuzzle(int roomNumber) {
        String query = "UPDATE Rooms SET north_exit=?, south_exit=?, east_exit=?, west_exit=? WHERE room_id=?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, determineNewExit("north", roomNumber));
            stmt.setInt(2, determineNewExit("south", roomNumber));
            stmt.setInt(3, determineNewExit("east", roomNumber));
            stmt.setInt(4, determineNewExit("west", roomNumber));
            stmt.setInt(5, roomNumber);

            stmt.executeUpdate();
            System.out.println("Room " + roomNumber + " unlocked after puzzle completion.");
        } catch (SQLException e) {
            LOGGER.severe("Error unlocking room " + roomNumber + ": " + e.getMessage());
        }
    }

    /** Determine new room exit status after puzzle completion **/
    private int determineNewExit(String direction, int roomNumber) {
        String query = "SELECT " + direction + "_exit FROM Rooms WHERE room_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, roomNumber); // Make sure this is set before executing

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int currentExit = rs.getInt(direction + "_exit");
                    return (currentExit == -1) ? roomNumber + 1 : currentExit; // Unlocks next room if blocked
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error determining new exit for room " + roomNumber + ": " + e.getMessage());
        }
        return -1; // Default: No valid exit found
    }

    /** Check if a puzzle is already solved **/
    public boolean isPuzzleSolved(int roomNumber) {
        Puzzle puzzle = getPuzzleFromRoom(roomNumber);
        return puzzle != null && puzzle.isSolved();
    }
}
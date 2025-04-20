package Controller;

import Model.Puzzle;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class PuzzleManager {
    private static final String DB_URL = "jdbc:sqlite:identifier.sqlite";
    private static final Logger LOGGER = Logger.getLogger(PuzzleManager.class.getName());
    private Map<Integer, Puzzle> puzzles;

    public PuzzleManager() {
        puzzles = new HashMap<>();
        loadPuzzlesFromDatabase(); // Load puzzles dynamically at runtime
    }

    /** Load puzzles from SQLite **/
    private void loadPuzzlesFromDatabase() {
        String query = "SELECT * FROM Puzzle";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int puzzleID = rs.getInt("puzzle_id");
                String puzzleName = rs.getString("puzzle_name");
                String puzzleDesc = rs.getString("puzzle_desc");
                String puzzleRiddle = rs.getString("puzzle_riddle");
                String puzzleAnswer = rs.getString("puzzle_answer");
                String puzzleReward = rs.getString("puzzle_reward");
                int roomNumber = rs.getInt("room_number");
                String postInteraction = rs.getString("post_interaction");

                Puzzle puzzle = new Puzzle(puzzleName, puzzleAnswer, puzzleRiddle, puzzleReward, puzzleDesc);
                puzzles.put(roomNumber, puzzle);
            }
            System.out.println("Puzzles loaded successfully.");
        } catch (SQLException e) {
            LOGGER.severe("Error loading puzzles from database: " + e.getMessage());
        }
    }

    /** Attempt to solve a puzzle **/
    public boolean attemptPuzzle(int roomNumber, String attempt) {
        Puzzle puzzle = puzzles.get(roomNumber);
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

    /** Determine new room exit status after puzzle completion **/
    private int determineNewExit(String direction, int roomNumber) {
        String query = "SELECT " + direction + "_exit FROM Rooms WHERE room_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, roomNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int currentExit = rs.getInt(direction + "_exit");
                return (currentExit == -1) ? roomNumber + 1 : currentExit; // Unlocks next room if previously blocked
            }
        } catch (SQLException e) {
            LOGGER.severe("Error determining new exit for room " + roomNumber + ": " + e.getMessage());
        }
        return -1; // Default: No valid exit found
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

    /** Check if a puzzle is already solved **/
    public boolean isPuzzleSolved(int roomNumber) {
        return puzzles.containsKey(roomNumber) && puzzles.get(roomNumber).isSolved();
    }
}
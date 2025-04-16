package Controller;/* Aaron Matthews ITEC 3860- Dr. Rahaf Baraket

 */

import Model.Puzzle;

import java.util.HashMap;
import java.util.Map;

public class PuzzleManager {
    private Map<String, Puzzle> puzzles;

    public PuzzleManager() {
        puzzles = new HashMap<>();
    }

    // Add a puzzle to the manager
    public void addPuzzle(String room, Puzzle puzzle) {
        puzzles.put(room, puzzle);
    }

    // Attempt to solve a puzzle in the given room
    public boolean attemptPuzzle(String room, String attempt) {
        Puzzle puzzle = puzzles.get(room);
        if (puzzle == null) {
            System.out.println("No puzzle exists in this room.");
            return false;
        }

        return puzzle.attemptSolution(attempt);
    }

    // Provide a hint for the puzzle in the given room
    public void requestHint(String room, int incorrectAttempts) {
        Puzzle puzzle = puzzles.get(room);
        if (puzzle != null && !puzzle.isSolved()) {
            puzzle.requestHint(incorrectAttempts);
        } else {
            System.out.println("No hints available for this room.");
        }
    }

    // Check if a puzzle in a room has been solved
    public boolean isPuzzleSolved(String room) {
        Puzzle puzzle = puzzles.get(room);
        return puzzle != null && puzzle.isSolved();
    }
}
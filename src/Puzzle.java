/* Aaron Matthews ITEC 3860- Dr. Rahaf Baraket

 */

public class Puzzle {
    private String description;
    private String solution;
    private String hint;
    private boolean isSolved;
    private String reward;
    private String location;

    public Puzzle(String description, String solution, String hint, String reward, String location) {
        this.description = description;
        this.solution = solution;
        this.hint = hint;
        this.reward = reward;
        this.location = location;
        this.isSolved = false;
    }

    public void describe() {
        System.out.println("Puzzle Location: " + location);
        System.out.println("Puzzle: " + description);
    }

    public boolean attemptSolution(String userAttempt) {
        if (isSolved) {
            System.out.println("This puzzle has already been solved.");
            return false;
        }

        if (userAttempt.equalsIgnoreCase(solution)) {
            isSolved = true;
            System.out.println("Correct! You solved the puzzle.");
            System.out.println("You received: " + reward);
            return true;
        } else {
            System.out.println("Incorrect answer. Try again or ask for a hint.");
            return false;
        }
    }

    public void requestHint(int incorrectAttempts) {
        if (incorrectAttempts >= 3) {
            System.out.println("Hint: " + hint);
        } else {
            System.out.println("You haven't made enough incorrect attempts for a hint.");
        }
    }

    public boolean isSolved() {
        return isSolved;
    }
}
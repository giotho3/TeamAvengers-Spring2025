package Model;

public class Puzzle {
    private String description;
    private String solution;
    private String hint;
    private boolean isSolved;
    private String reward;
    private String location;
    private int failedAttempts;

    public Puzzle(String description, String solution, String hint, String reward, String location) {
        this.description = description;
        this.solution = solution;
        this.hint = hint;
        this.reward = reward;
        this.location = location;
        this.isSolved = false;
        this.failedAttempts = 0;
    }

    /** Display puzzle details **/
    public void describe() {
        System.out.println("Puzzle Location: " + location);
        System.out.println("Puzzle: " + description);
    }

    /** Attempt to solve the puzzle **/
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
            failedAttempts++;
            System.out.println("Incorrect answer. Try again.");

            if (failedAttempts >= 3) {
                System.out.println("Hint: " + hint);
            }

            return false;
        }
    }

    /** Request a hint dynamically **/
    public void requestHint() {
        if (failedAttempts >= 3) {
            System.out.println("Hint: " + hint);
        } else {
            System.out.println("Keep trying! More hints become available after multiple incorrect attempts.");
        }
    }

    /** Getters for encapsulated access **/
    public String getDescription() { return description; }
    public String getReward() { return reward; }
    public boolean isSolved() { return isSolved; }
}
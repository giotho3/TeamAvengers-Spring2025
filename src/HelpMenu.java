/* Aaron Matthews ITEC 3860- Dr. Rahaf Baraket
This class contains Functional Requirement 7
 */

import java.util.HashMap;
import java.util.Map;

public class HelpMenu {
    private Map<String, String> helpSections;

    public HelpMenu() {
        helpSections = new HashMap<>();
        initializeHelp();
    }

    private void initializeHelp() {
        helpSections.put("navigation", "Use commands: North, East, South, West, Enter, Exit, Look Around.");
        helpSections.put("puzzle", "Interact with puzzles using the 'interact' command. Request hints with 'hint'.");
        helpSections.put("combat", "Use 'fight' to engage, 'attack' to deal damage, and 'flee' to escape battles.");
        helpSections.put("items", "Equip weapons, armor, potions via inventory management. Use 'items' command.");
        helpSections.put("story", "Your journey unfolds as you explore and survive against challenges.");
        helpSections.put("save", "Manually save progress in designated rooms. Use 'save' command when prompted.");
    }

    public void displayHelp(String topic) {
        if (helpSections.containsKey(topic)) {
            System.out.println(helpSections.get(topic));
        } else {
            System.out.println("Help topics: navigation, puzzle, combat, items, story, save.");
        }
    }

    public void exitHelpMenu() {
        System.out.println("Exiting help menu...");
    }
}
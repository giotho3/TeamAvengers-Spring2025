package View;

/**Class: Model.ItemModel
 * Giovanni Thomas
 * Course: ITEC 3860 Spring 2025
 * Written: April 10, 2025
 *
 * The ItemView Class is responsible for displaying Item-Related information to the user.
 */

import Model.Item;
import java.util.ArrayList;

public class ItemView {

    public void displayItemDetails(Item item) {
        System.out.println("Item: " + item.getName());
        System.out.println("Description: " + item.getDesc());
        System.out.println("Modifier: " + item.getModifier());
    }

    public void displayInventory(ArrayList<Item> inventory) {
        if (inventory.isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            System.out.println("Inventory:");
            for (Item item : inventory) {
                System.out.println(item.getName());
            }

        }
    }

    public void displayPickupMessage(Item item) {
        System.out.println("You picked up: " + item.getName());
    }

    public void displayUseItemResult(Item item, boolean canUse) {
        if (canUse) {
            System.out.println("You used the " + item.getName() + ".");
        } else {
            System.out.println("You can't use the " + item.getName() + " right now.");
        }
    }

    public void displayError(String message) {
        System.out.println("Error: " + message);
    }
}



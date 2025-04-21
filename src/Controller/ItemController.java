package Controller;

/**Class: Model.ItemController
 * Giovanni Thomas
 * Course: ITEC 3860 Spring 2025
 * Written: April 10, 2025
 *
 * The ItemController Class is responsible relaying commands to the Item Model and managing how items are
 * added and removed from the inventory.
 */

import Model.Item;
import Model.Player;
import View.ItemView;

import java.util.ArrayList;

public class ItemController {

    private final Player player;
    private final ItemView itemView;
    private final ArrayList<Item> inventory = new ArrayList<>();

    public ItemController(Player player, ItemView itemView) {
        this.player = player;
        this.itemView = itemView;
    }

    public void addToInventory(Item item) {
        inventory.add(item);
        itemView.displayPickupMessage(item);
    }

    public void showInventory() {
        itemView.displayInventory(inventory);
    }

    public void inspectItem(int index) {
        if (index >= 0 && index < inventory.size()) {
            itemView.displayItemDetails(inventory.get(index));
        } else {
            itemView.displayError("That is not in your inventory");
        }
    }
}

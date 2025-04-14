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
import View.ItemView;

import java.util.ArrayList;

public class ItemController {

    private final Player player;
    private final ItemView itemView;

    public ItemController(Player player, ItemView itemView) {
        this.player = player;
        this.itemView = itemView;
    }

    public void addItemToInventory(Item item) {
        player.getInventory().add(item);
        itemView.displayPickupMessage(item);
    }

    public void removeItemFromInventory(Item item) {
        player.getInventory().remove(item);
    }

    public void useItem(Item item) {
        if (player.getInventory().contains(item)) {
            item.applyModifier(player); // Assume your Item class has logic for this
            removeItemFromInventory(item);
            itemView.displayUseItemResult(item, true);
        } else {
            itemView.displayUseItemResult(item, false);
        }
    }

    public void showInventory() {
        itemView.displayInventory(player.getInventory());
    }

    public void inspectItem(int index) {
        ArrayList<Item> inventory = player.getInventory();
        if (index >= 0 && index < inventory.size()) {
            itemView.displayItemDetails(inventory.get(index));
        } else {
            itemView.displayError("That is not in your inventory");
        }
    }
}

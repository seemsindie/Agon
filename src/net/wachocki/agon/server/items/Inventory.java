package net.wachocki.agon.server.items;

import net.wachocki.agon.common.network.Network;
import net.wachocki.agon.server.entity.Player;

/**
 * User: Marty
 * Date: 10/27/13
 * Time: 1:35 AM
 */
public class Inventory {

    private int inventorySize = 15;

    private Item[] items;

    public Inventory() {
        this.items = new Item[inventorySize];
    }

    public Inventory(Item[] items) {
        this.items = items;
    }

    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }

    public void setItem(int index, Item item) {
        items[index] = item;
    }

    public Item getItem(int index) {
        return items[index];
    }

    public int getInventorySize() {
        return inventorySize;
    }

    public void setInventorySize(int inventorySize) {
        this.inventorySize = inventorySize;
    }

    public int addItem(Item item) {
        int slot = getFirstFreeSlot();
        if (slot != -1) {
            setItem(slot, item);
            return slot;
        }
        return -1;
    }

    public int getFirstFreeSlot() {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                return i;
            }
        }
        return -1;
    }

    public int getFreeSlots() {
        int free = 0;
        for (Item item : items) {
            if (item == null) {
                free++;
            }
        }
        return free;
    }

    public boolean isFull() {
        return getFreeSlots() == 0;
    }

    public void sendUpdate(Player player, int[] slots) {
        int[] itemIds = new int[slots.length];
        int[] itemAmounts = new int[slots.length];
        for (int i = 0; i < itemIds.length; i++) {
            itemIds[i] = items[slots[i]].getItemId();
            itemAmounts[i] = items[slots[i]].getAmount();
        }
        Network.UpdateInventory updateInventory = new Network.UpdateInventory();
        updateInventory.itemIds = itemIds;
        updateInventory.itemAmounts = itemAmounts;
        updateInventory.slots = slots;
        player.getConnection().sendTCP(updateInventory);
    }

}

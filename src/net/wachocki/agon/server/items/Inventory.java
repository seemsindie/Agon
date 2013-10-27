package net.wachocki.agon.server.items;

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

    public int getFreeSlots() {
        int free = 0;
        for(Item item : items) {
            if(item == null) {
                free++;
            }
        }
        return free;
    }

}

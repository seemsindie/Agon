package net.wachocki.agon.client.items;

import net.wachocki.agon.client.GameClient;
import org.newdawn.slick.GameContainer;

/**
 * User: Marty
 * Date: 10/27/13
 * Time: 1:35 AM
 */
public class Inventory {

    private GameClient game;
    private GameContainer gameContainer;
    private int inventorySize = 15;
    private Item[] items;

    public Inventory(GameClient game, GameContainer gameContainer) {
        this.game = game;
        this.gameContainer = gameContainer;
        this.items = new Item[inventorySize];
    }

    public Inventory(GameClient game, GameContainer gameContainer, Item[] items) {
        this.game = game;
        this.gameContainer = gameContainer;
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

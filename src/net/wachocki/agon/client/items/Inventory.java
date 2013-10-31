package net.wachocki.agon.client.items;

import net.wachocki.agon.client.GameClient;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;

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
    private boolean visible;
    private Image image;

    public Inventory(Image image, GameClient game, GameContainer gameContainer) {
        this.image = image;
        this.game = game;
        this.gameContainer = gameContainer;
        this.items = new Item[inventorySize];
    }

    public Inventory(Image image, GameClient game, GameContainer gameContainer, Item[] items) {
        this.image = image;
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

    public boolean addItem(Item item) {
        int slot = getFirstFreeSlot();
        if (slot != -1) {
            setItem(slot, item);
            return true;
        }
        return false;
    }

    public int getFirstFreeSlot() {
        for (int i = 0; i < items.length; i++) {
            if (items == null) {
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

    public boolean isVisible() {
        return visible;
    }

    public void toggle() {
        visible = !visible;
    }

    public void render(GameClient game, GameContainer gameContainer) {
        int inventoryX = gameContainer.getWidth() - image.getWidth();
        int inventoryY = gameContainer.getHeight() - image.getHeight();
        image.draw(inventoryX, inventoryY);
        int row = 0;
        int column = 0;
        for(int i = 0; i < items.length; i++) {
            if(items[i] != null) {
                items[i].getDefinition().getInventoryImage().draw(inventoryX + 8 + (row * 58), inventoryY + 10 + (column * 58));
                row++;
                if(row == 5) {
                    row = 0;
                    column++;
                }
            }
        }
    }

}

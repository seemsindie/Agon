package net.wachocki.agon.client.items;

/**
 * User: Marty
 * Date: 10/27/13
 * Time: 1:36 AM
 */
public class Item {

    private int id;
    private int amount;

    public Item(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

package net.wachocki.agon.server.items;

import net.wachocki.agon.common.network.Network;
import net.wachocki.agon.server.entity.Entity;
import net.wachocki.agon.server.entity.Player;
import org.newdawn.slick.geom.Vector2f;

/**
 * User: Marty
 * Date: 10/27/13
 * Time: 1:43 AM
 */
public class GroundItem extends Entity {

    private int groundId;
    private Vector2f position;
    private Item item;

    public GroundItem(Item item, int groundId, Vector2f position) {
        super("");
        this.item = item;
        this.groundId = groundId;
        setPosition(position);
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public int getGroundId() {
        return groundId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void sendRemove(Player[] players, Player retriever) {
        for (Player player : players) {
            Network.RemoveGroundItem removeGroundItem = new Network.RemoveGroundItem();
            removeGroundItem.playerName = retriever.getName();
            removeGroundItem.groundId = groundId;
            player.getConnection().sendTCP(removeGroundItem);
        }
    }
}

package net.wachocki.agon.client.entity;

import net.wachocki.agon.client.GameClient;
import net.wachocki.agon.client.interfaces.Interactable;
import net.wachocki.agon.client.items.Item;
import net.wachocki.agon.common.network.Network;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 * User: Marty
 * Date: 10/27/13
 * Time: 1:43 AM
 */
public class GroundItem extends Entity implements Interactable {

    private int groundId;
    private Vector2f position;
    private Item item;

    public GroundItem(int groundId, Item item, Vector2f position) throws SlickException {
        super(item.getDefinition().getName());
        this.groundId = groundId;
        this.item = item;
        setPosition(position);
        setImage(item.getDefinition().getGroundImage());
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

    public Vector2f getScreenPosition(GameClient game) {
        Vector2f screenPosition = game.getCamera().worldToScreen(position);
        float x = screenPosition.getX() - (getImage().getWidth() / 2);
        float y = screenPosition.getY() - (getImage().getHeight() / 2);
        return new Vector2f(x, y);
    }

    public Rectangle getScreenBounds(GameClient game) {
        Vector2f screenPosition = getScreenPosition(game);
        return new Rectangle(screenPosition.getX(), screenPosition.getY(), getImage().getWidth(), getImage().getHeight());
    }

    public void render(GameClient game, GameContainer gameContainer) {
        Vector2f screenPosition = getScreenPosition(game);
        getImage().draw(screenPosition.getX(), screenPosition.getY());
        gameContainer.getGraphics().setColor(getItem().getDefinition().getItemQuality().color);
        gameContainer.getGraphics().drawString(getItem().getDefinition().getName(), screenPosition.getX(), screenPosition.getY() - 15);
    }

    @Override
    public String[] getActions() {
        return new String[] { "Pickup " + item.getDefinition().getName() };
    }

    @Override
    public void doAction(GameClient game, int actionIndex) {
        if (actionIndex == 0) {
            if (game.getPlayer().getPosition().distance(getPosition()) > 1) {
                game.getPlayer().requestWalk(game, getPosition());
                game.setTargetEntity(this);
                game.setTargetEntityActionIndex(actionIndex);
            } else {
                Network.RemoveGroundItem removeGroundItem = new Network.RemoveGroundItem();
                removeGroundItem.playerName = game.getPlayer().getName();
                removeGroundItem.groundId = getGroundId();
                game.getClient().sendUDP(removeGroundItem);
            }
        }
    }
}

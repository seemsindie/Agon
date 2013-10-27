package net.wachocki.agon.client.items;

import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

/**
 * User: Marty
 * Date: 10/27/13
 * Time: 1:43 AM
 */
public class GroundItem extends Item {

    private Vector2f position;

    public GroundItem(int id, int amount, Vector2f position) {
        super(id, amount);
        this.position = position;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public void render(Game game, GameContainer gameContainer) {

    }

}

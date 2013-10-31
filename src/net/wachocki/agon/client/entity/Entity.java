package net.wachocki.agon.client.entity;

import net.wachocki.agon.client.GameClient;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

/**
 * User: Marty
 * Date: 10/25/13
 * Time: 12:14 PM
 */
public class Entity {

    private String name;
    private Vector2f position;
    private Image image;

    public Entity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Vector2f getScreenPosition(GameClient game) {
        Vector2f screenPosition = game.getCamera().worldToScreen(position);
        float x = screenPosition.getX() - (image.getWidth() / 2);
        float y = screenPosition.getY() - (image.getWidth() / 2);
        return new Vector2f(x, y);
    }

    public void render(GameClient game, GameContainer gameContainer) {
        Vector2f screenPosition = getScreenPosition(game);
        image.draw(screenPosition.getX(), screenPosition.getY());
    }
}

package net.wachocki.agon.client.camera;

import net.wachocki.agon.client.GameClient;
import net.wachocki.agon.client.entity.Entity;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

/**
 * User: Marty
 * Date: 9/16/13
 * Time: 11:36 PM
 */
public class Camera {

    private GameClient game;
    private GameContainer gameContainer;
    private int x = 0;
    private int y = 0;
    private float zoom = 1F;

    public Camera(GameClient game, GameContainer gameContainer) {
        this.game = game;
        this.gameContainer = gameContainer;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public void centerOn(Entity entity) {
        Vector2f position = worldToScreen(entity.getPosition());
        x = (int) position.getX() + getX() - (gameContainer.getWidth() / 2);
        y = (int) position.getY() + getY() - (gameContainer.getHeight() / 2);
    }

    public Vector2f worldToScreen(Vector2f position) {
        return new Vector2f(position.getX() * game.getTileMap().getTileWidth() - getX(), position.getY() * game.getTileMap().getTileHeight() - getY());
    }

    public Vector2f screenToWorld(Vector2f position) {
        return new Vector2f(position.getX() / game.getTileMap().getTileWidth(), position.getY() / game.getTileMap().getTileHeight());
    }

}

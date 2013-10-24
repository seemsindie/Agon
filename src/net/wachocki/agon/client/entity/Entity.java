package net.wachocki.agon.client.entity;

import net.wachocki.agon.client.GameClient;
import org.newdawn.slick.geom.Vector2f;

/**
 * User: Marty
 * Date: 10/24/13
 * Time: 2:49 AM
 */
public class Entity {

    private String name;
    private Vector2f position;

    public Entity(String name) {
        this.name = name;
        this.position = new Vector2f(100, 100);
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

    public void render(GameClient game) {
        if(this instanceof Player) {
            Player player = (Player) this;
            game.getSpritesSheets().get(player.getSpecialization()).getSprite(0, 0).draw(position.x, position.y);
        }
    }

}

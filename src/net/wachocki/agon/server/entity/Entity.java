package net.wachocki.agon.server.entity;

import org.newdawn.slick.geom.Vector2f;

/**
 * User: Marty
 * Date: 10/25/13
 * Time: 12:16 PM
 */
public class Entity {

    private String name;
    private Vector2f position;

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

}

package net.wachocki.agon.server.entity;

import org.newdawn.slick.geom.Vector2f;

/**
 * User: Marty
 * Date: 10/24/13
 * Time: 2:49 AM
 */
public class Entity {

    private String name;
    private Vector2f position;
    private Vector2f destination;

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

    public Vector2f getDestination() {
        return destination;
    }

    public void setDestination(Vector2f destination) {
        this.destination = destination;
    }
}

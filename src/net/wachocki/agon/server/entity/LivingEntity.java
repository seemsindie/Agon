package net.wachocki.agon.server.entity;

import org.newdawn.slick.geom.Vector2f;

import java.util.LinkedList;

/**
 * User: Marty
 * Date: 10/24/13
 * Time: 2:49 AM
 */
public class LivingEntity extends Entity {

    private String name;
    private Vector2f position;
    private LinkedList<Vector2f> walkingQueue = new LinkedList<Vector2f>();

    public LivingEntity(String name) {
        super(name);
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

    public LinkedList<Vector2f> getWalkingQueue() {
        return walkingQueue;
    }
}
